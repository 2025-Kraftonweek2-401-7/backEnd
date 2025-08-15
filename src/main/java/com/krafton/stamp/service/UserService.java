package com.krafton.stamp.service;

import com.krafton.stamp.domain.User;
import com.krafton.stamp.dto.UserResponseDto;
import com.krafton.stamp.dto.UserSignupRequestDto;
import com.krafton.stamp.dto.UserUpdateRequestDto;
import com.krafton.stamp.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    // 비밀번호 해시가 필요 없다면 제거해도 됨. (추후 JWT 붙일 때 권장)
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto signup(UserSignupRequestDto req) {
        // 중복 체크 등은 필요 시 추가
        User user = User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .score(0)
                .build();
        return new UserResponseDto(userRepository.save(user));
    }

    public UserResponseDto getById(Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserResponseDto(u);
    }

    @Transactional
    public UserResponseDto updateUser(UserUpdateRequestDto req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        user.updateInfo(req.getUsername(), req.getEmail());
        user.updatePassword(req.getPassword(), passwordEncoder);

        return new UserResponseDto(user);
    }


    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
        userRepository.delete(user);
    }

}
