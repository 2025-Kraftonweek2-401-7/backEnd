package com.krafton.stamp.security;

import com.krafton.stamp.domain.User;
import com.krafton.stamp.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository; // ⭐️ 추가 필요
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        // ✅ 로그인과 OAuth2 경로는 JWT 필터 제외
        if (uri.startsWith("/login") || uri.startsWith("/oauth2")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearer = request.getHeader("Authorization");

        try {
            if (bearer != null && bearer.startsWith("Bearer ")) {
                String token = bearer.substring(7);

                if (jwtTokenProvider.validateToken(token)) {
                    String userId = jwtTokenProvider.getUserId(token);

                    User user = userRepository.findById(Long.parseLong(userId))
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    PrincipalUser principalUser = new PrincipalUser(user);

                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(principalUser, null, authorities);

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            logger.warn("JWT 인증 실패: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or expired token.\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }

}

