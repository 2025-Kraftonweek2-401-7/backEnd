package com.krafton.stamp.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

//확장프로그램
@RestController
public class OAuth2ExtensionController {

    @GetMapping("/oauth2/extension-login")
    public void extensionLogin(@RequestParam("redirect_uri") String redirectUri,
                               HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        // 보안: chromiumapp.org만 허용
        if (!isAllowedChromiumAppUrl(redirectUri)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "invalid redirect_uri");
            return;
        }
        request.getSession(true).setAttribute("ext_redirect_uri", redirectUri);
        response.sendRedirect("/oauth2/authorization/google");
    }

    private boolean isAllowedChromiumAppUrl(String uri) {
        try {
            var u = java.net.URI.create(uri);
            return "https".equalsIgnoreCase(u.getScheme())
                    && u.getHost() != null
                    && u.getHost().endsWith(".chromiumapp.org");
        } catch (Exception e) {
            return false;
        }
    }
}
