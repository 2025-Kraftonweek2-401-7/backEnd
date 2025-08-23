package com.krafton.stamp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class JwtOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    /** 로컬/스웨거 테스트용 콜백 페이지 */
    private static final String DEFAULT_LOCAL_PAGE = "/jwt-test.html";

    /** 확장 redirect URI를 임시로 담아둘 세션키(사전 저장 로직과 짝) */
    private static final String SESSION_KEY = "ext_redirect_uri";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Principal은 커스텀 PrincipalUser 또는 OAuth2User일 수 있음 → 두 케이스 모두 처리
        Long userId = null;
        String email = null;

        Object principal = authentication.getPrincipal();
        if (principal instanceof PrincipalUser pu) {
            userId = pu.getUser().getId();
            email = pu.getUser().getEmail();
        } else if (principal instanceof OAuth2User oAuth2User) {
            // 혹시 PrincipalUser 매핑 전에 이 핸들러가 호출되는 환경 대비
            Object em = oAuth2User.getAttributes().get("email");
            if (em == null) em = oAuth2User.getAttributes().get("preferred_username");
            email = em != null ? String.valueOf(em) : null;

            Object sub = oAuth2User.getAttributes().get("sub");
            try {
                if (sub != null && String.valueOf(sub).matches("\\d+")) {
                    userId = Long.valueOf(String.valueOf(sub));
                }
            } catch (Exception ignore) {}
        }

        if (userId == null || email == null || email.isBlank()) {
            // 안전장치: 최소한 이메일은 있어야 토큰 발급 가능
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "OAuth2 principal missing required attributes");
            return;
        }

        // ✅ 이메일 포함 JWT 발급
        String token = jwtTokenProvider.createToken(userId, email);

        // 1) 세션에서 확장 리다이렉트 URI 꺼내기(있으면 우선)
        HttpSession session = request.getSession(false);
        String extRedirect = null;
        if (session != null) {
            Object v = session.getAttribute(SESSION_KEY);
            if (v instanceof String s) {
                extRedirect = s;
            }
            session.removeAttribute(SESSION_KEY);
        }

        // 2) 허용되는 확장 리다이렉트면 그쪽으로 전달
        if (isAllowedExtensionRedirect(extRedirect)) {
            String location = extRedirect + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", location);
            return;
        }

        // 3) 아니면 로컬 테스트 페이지로 폴백
        response.sendRedirect(DEFAULT_LOCAL_PAGE + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8));
    }

    /** 확장용 redirect 화이트리스트 (Chrome Identity redirect) */
    private boolean isAllowedExtensionRedirect(String uri) {
        if (uri == null || uri.isBlank()) return false;
        try {
            URI u = URI.create(uri);
            return "https".equalsIgnoreCase(u.getScheme())
                    && u.getHost() != null
                    && u.getHost().endsWith(".chromiumapp.org");
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
