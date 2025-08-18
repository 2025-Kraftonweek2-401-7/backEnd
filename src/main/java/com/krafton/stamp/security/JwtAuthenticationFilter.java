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
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private boolean isWhitelisted(String uri) {
        return uri.equals("/") ||
                uri.equals("/swagger-ui.html") ||
                uri.startsWith("/swagger-ui") ||
                uri.equals("/v3/api-docs") ||
                uri.startsWith("/v3/api-docs") ||
                uri.startsWith("/swagger-resources") ||
                uri.startsWith("/webjars") ||
                uri.startsWith("/h2-console") ||
                uri.startsWith("/login") ||
                uri.startsWith("/oauth2") ||
                uri.startsWith("/api/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (isWhitelisted(uri)) { chain.doFilter(req, res); return; }

        String bearer = req.getHeader("Authorization");
        try {
            if (bearer != null && bearer.startsWith("Bearer ")) {
                var token = bearer.substring(7);
                if (jwtTokenProvider.validateToken(token)) {
                    var userId = jwtTokenProvider.getUserId(token);
                    var user = userRepository.findById(Long.parseLong(userId))
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    var auth = new UsernamePasswordAuthenticationToken(
                            new PrincipalUser(user), null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        } catch (Exception e) {
            // ❌ 여기서 401 쓰지 말 것
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(req, res);
    }

}


