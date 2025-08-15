package com.krafton.stamp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupRequestDto {
    @NotBlank
    private String username;
    @Email
    private String email;
    @NotBlank
    private String password;
}