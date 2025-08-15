package com.krafton.stamp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "users")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    private int score = 0;// 우표 수집으로 얻는 점수

    // ✅ 닉네임 / 이메일 수정용 메서드
    public void updateInfo(String username, String email) {
        if (username != null && !username.isBlank()) this.username = username;
        if (email != null && !email.isBlank()) this.email = email;
    }

    // ✅ 비밀번호 수정용 메서드
    public void updatePassword(String newPassword, PasswordEncoder encoder) {
        if (newPassword != null && !newPassword.isBlank()) {
            this.password = encoder.encode(newPassword);
        }
    }

}
