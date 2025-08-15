package com.krafton.stamp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserUpdateRequestDto {
    private Long userId;       // ✅ JWT 사용 시 제거 가능
    private String username;
    private String email;
    private String password;
}
