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

    // 🔽 소셜 로그인 사용자는 비밀번호가 없을 수 있으므로 nullable = true
    @Column(nullable = true)
    private String password;

    @Builder.Default
    private int score = 0; // 우표 수집 점수

    // ✅ 소셜 로그인 관련 필드
    private String provider;     // ex: "google"
    private String providerId;   // ex: Google의 sub 값
    private String profileImage; // 구글 프로필 사진 URL

    // ✅ 닉네임 / 이메일 수정용
    public void updateInfo(String username, String email) {
        if (username != null && !username.isBlank()) this.username = username;
        if (email != null && !email.isBlank()) this.email = email;
    }

    // ✅ 비밀번호 수정용 (로컬 유저용)
    public void updatePassword(String newPassword, PasswordEncoder encoder) {
        if (newPassword != null && !newPassword.isBlank()) {
            this.password = encoder.encode(newPassword);
        }
    }

    // ✅ 프로필 이미지 변경 등
    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public User(String username, String email, String password, int score,
                String provider, String providerId, String profileImage) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.score = score;
        this.provider = provider;
        this.providerId = providerId;
        this.profileImage = profileImage;
    }

}
