package com.krafton.stamp.security;

import com.krafton.stamp.domain.User;
import com.krafton.stamp.security.JwtTokenProvider;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        PrincipalUser principal = (PrincipalUser) authentication.getPrincipal();
        User user = principal.getUser();

        String token = jwtTokenProvider.createToken(String.valueOf(user.getId()), user.getEmail());

        // 프론트엔드로 토큰 전달 (여기서는 리다이렉트 + 쿼리파라미터로 예시)
        response.sendRedirect("/jwt-test.html?token=" + token);// 또는 응답에 직접 write도 가능
        System.out.println("✅ Authentication principal class: " + authentication.getPrincipal().getClass());

    }
}
