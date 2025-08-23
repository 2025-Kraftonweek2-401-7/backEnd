package com.krafton.stamp.security;

import com.krafton.stamp.domain.User;
import com.krafton.stamp.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private boolean isWhitelisted(String uri) {
        return uri.equals("/")
                || uri.equals("/swagger-ui.html")
                || uri.startsWith("/swagger-ui")
                || uri.equals("/v3/api-docs")
                || uri.startsWith("/v3/api-docs")
                || uri.startsWith("/swagger-resources")
                || uri.startsWith("/webjars")
                || uri.startsWith("/h2-console")
                || uri.startsWith("/login")
                || uri.startsWith("/oauth2")
                || uri.startsWith("/api/auth")
                || uri.startsWith("/api/public")
                || uri.equals("/actuator/health")
                // 확장 연동 엔드포인트가 공개라면 추가
                || uri.startsWith("/api/extension/redirect")
                || uri.startsWith("/api/extension/code-exchange");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        final String uri = req.getRequestURI();

        // 1) 화이트리스트는 그대로 통과
        if (isWhitelisted(uri)) {
            chain.doFilter(req, res);
            return;
        }

        // 2) 프리플라이트는 무조건 통과
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            chain.doFilter(req, res);
            return;
        }

        final String auth = req.getHeader("Authorization");

        // 3) 헤더 없거나 포맷 불일치 → 인증 시도 없이 다음 필터로 (여기서 401 내지 않음)
        if (auth == null || !auth.startsWith("Bearer ")) {
            chain.doFilter(req, res);
            return;
        }

        final String token = auth.substring(7).trim();

        try {
            // 4) JWT 유효성 체크 (만료/서명 등)
            if (!jwtTokenProvider.validateToken(token)) {
                // 유효하지 않으면 인증 설정 없이 통과 → 최종 엔트리포인트가 처리
                chain.doFilter(req, res);
                return;
            }

            // 5) email 우선 → 없거나 실패하면 sub(id)로 조회
            String email = null;
            try {
                email = jwtTokenProvider.getEmail(token); // 토큰에 email 클레임이 있어야 함
            } catch (Exception ignore) {
                // provider에 메서드 없거나 클레임 없을 수 있음
            }

            User user = null;

            if (email != null && !email.isBlank()) {
                user = userRepository.findByEmail(email).orElse(null);
            }

            if (user == null) {
                String sub = null;
                try {
                    sub = jwtTokenProvider.getUserId(token);
                    if (sub != null && sub.matches("\\d+")) {
                        user = userRepository.findById(Long.parseLong(sub)).orElse(null);
                    }
                } catch (Exception ignore) {
                }
                log.debug("JWT resolved => email={}, sub={}", email, sub);
            } else {
                log.debug("JWT resolved => email={}, sub=(skip)", email);
            }

            if (user == null) {
                // 사용자 미존재: 여기서 401 내지 말고 패스 → 컨트롤러 단에서 인증 필요시 401
                log.warn("JWT valid but user not found. email={}, uri={}", email, uri);
                chain.doFilter(req, res);
                return;
            }

            // 6) 인증 컨텍스트 세팅
            var principal = new PrincipalUser(user);
            var authToken = new UsernamePasswordAuthenticationToken(
                    principal, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
            SecurityContextHolder.getContext().setAuthentication(authToken);

        } catch (Exception ex) {
            // 어떤 예외든 컨텍스트 비우고 다음 필터로
            log.warn("JWT processing failed: {}", ex.getMessage());
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(req, res);
    }
}
