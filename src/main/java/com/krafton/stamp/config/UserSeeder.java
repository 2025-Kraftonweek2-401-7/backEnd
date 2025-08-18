package com.krafton.stamp.config;

import com.krafton.stamp.domain.User;
import com.krafton.stamp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component

@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        addUserIfNotExists("testuser", "test@example.com", "1234");
    }

    private void addUserIfNotExists(String username, String email, String rawPassword) {
        userRepository.findByEmail(email).orElseGet(() ->
                userRepository.save(User.builder()
                        .username(username)
                        .email(email)
                        .password(passwordEncoder.encode(rawPassword))
                        .score(0)
                        .build()
                )
        );
    }
}
