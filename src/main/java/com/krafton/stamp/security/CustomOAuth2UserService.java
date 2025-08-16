package com.krafton.stamp.security;

import com.krafton.stamp.domain.User;
import com.krafton.stamp.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.*;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.user.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        System.out.println("✅ CustomOAuth2UserService 등록됨");
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(request);

        String registrationId = request.getClientRegistration().getRegistrationId(); // google
        String userNameAttribute = request.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName(); // 보통 "sub"

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String providerId = (String) attributes.get("sub");
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        String picture = (String) attributes.get("picture");

        // username은 email 앞부분에서 파생 (or name 사용)
        String username = email != null ? email.split("@")[0] : name;

        // 이미 가입된 유저인지 확인 후 저장 또는 업데이트
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.builder()
                                .username(username)
                                .email(email)
                                .password(null) // 소셜 로그인은 패스워드 없음
                                .provider(registrationId)
                                .providerId(providerId)
                                .profileImage(picture)
                                .build()
                ));
        return new PrincipalUser(user, attributes);
    }
}
