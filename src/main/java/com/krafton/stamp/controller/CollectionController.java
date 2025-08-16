package com.krafton.stamp.controller;

import com.krafton.stamp.dto.StampCollectRequestDto;
import com.krafton.stamp.dto.UserStampResponseDto;
import com.krafton.stamp.security.PrincipalUser;
import com.krafton.stamp.service.StampService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "My Collection", description = "내 우표 수집/조회 API")
public class CollectionController {

    private final StampService stampService;


    @GetMapping("/api/me/stamps")
    @Operation(summary = "내 우표 목록", description = "내가 수집한 우표들을 반환합니다.")
    public ResponseEntity<List<UserStampResponseDto>> myStamps(
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        Long userId = principalUser.getUser().getId();
        var list = stampService.getMyStamps(userId)
                .stream().map(UserStampResponseDto::new).toList();
        return ResponseEntity.ok(list);
    }


    @PostMapping("/api/me/stamps/collect")
    @Operation(summary = "우표 수집", description = "우표를 수집합니다 (이미 있으면 수량 증가)")
    public ResponseEntity<Void> collect(@RequestBody StampCollectRequestDto req) {
        stampService.collectStamp(req.getUserId(), req.getStampId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/users/{userId}/stamps")
    @Operation(summary = "다른 사용자의 우표 목록", description = "다른 사용자가 수집한 우표들을 반환합니다.")
    public ResponseEntity<List<UserStampResponseDto>> getUserStamps(@PathVariable Long userId) {
        var list = stampService.getMyStamps(userId)
                .stream().map(UserStampResponseDto::new).toList();
        return ResponseEntity.ok(list);
    }
}
