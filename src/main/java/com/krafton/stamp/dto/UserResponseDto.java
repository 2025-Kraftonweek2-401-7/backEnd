package com.krafton.stamp.dto;

import com.krafton.stamp.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    private final Long id;
    private final String username;
    private final String email;
    private final int score;

    public UserResponseDto(User u) {
        this.id = u.getId();
        this.username = u.getUsername();
        this.email = u.getEmail();
        this.score = u.getScore();
    }
}