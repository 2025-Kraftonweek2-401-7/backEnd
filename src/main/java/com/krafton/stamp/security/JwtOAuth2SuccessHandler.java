package com.krafton.stamp.security;

import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    private static final String DEFAULT_LOCAL_PAGE = "/jwt-test.html"; // 로컬/스웨거 테스트용
    private static final String SESSION_KEY = "ext_redirect_uri";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, jakarta.servlet.ServletException {

        var principal = (PrincipalUser) authentication.getPrincipal();
        var user = principal.getUser();
        String token = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getEmail());

        // 1) 세션에서 확장 리다이렉트 URI 꺼내기
        HttpSession session = request.getSession(false);
        String extRedirect = null;
        if (session != null) {
            Object v = session.getAttribute(SESSION_KEY);
            if (v instanceof String s) {
                extRedirect = s;
            }
            // 사용 후 정리(선택)
            session.removeAttribute(SESSION_KEY);
        }

        // 2) 허용되는 확장 리다이렉트면 거기로 토큰 전달
        if (isAllowedExtensionRedirect(extRedirect)) {
            String location = extRedirect + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8);
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", location);
            return;
        }

        // 3) 아니면 로컬 테스트 페이지로 폴백
        response.sendRedirect(DEFAULT_LOCAL_PAGE + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8));
    }

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
