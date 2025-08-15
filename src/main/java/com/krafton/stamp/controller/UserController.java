package com.krafton.stamp.controller;

import com.krafton.stamp.dto.UserResponseDto;
import com.krafton.stamp.dto.UserSignupRequestDto;
import com.krafton.stamp.dto.UserUpdateRequestDto;
import com.krafton.stamp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "회원 API")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "회원 가입", description = "사용자를 생성합니다.")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserSignupRequestDto req) {
        return ResponseEntity.ok(userService.signup(req));
    }

    @GetMapping("/{id}")
    @Operation(summary = "회원 조회", description = "ID로 사용자를 조회합니다.")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "회원 정보 수정", description = "사용자의 정보를 수정합니다.")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDto req) {
        req.setUserId(id);
        return ResponseEntity.ok(userService.updateUser(req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "회원 삭제", description = "사용자를 삭제합니다.")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
