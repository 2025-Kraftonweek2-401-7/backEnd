package com.krafton.stamp.dto;

import com.krafton.stamp.domain.User;
import lombok.Getter;

@Getter
public class OAuthUserResponseDto {
    private final Long id;
    private final String username;
    private final String email;
    private final int score;
    private final String profileImage;
    private final String provider;

    public OAuthUserResponseDto(User u) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.email = u.getEmail();
        this.score = u.getScore();
        this.profileImage = u.getProfileImage();
        this.provider = u.getProvider();
    }
}
