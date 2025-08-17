package com.krafton.stamp.controller;

import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.dto.StampCollectRequestDto;
import com.krafton.stamp.dto.UserStampResponseDto;
import com.krafton.stamp.security.PrincipalUser;
import com.krafton.stamp.service.StampService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @Operation(summary = "우표 수집", description = "같은 우표를 다시 수집하면 count가 증가합니다.")
    public ResponseEntity<Void> collect(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody @Valid StampCollectRequestDto req
    ) {
        Long userId = principalUser.getUser().getId();
        stampService.collectStamp(userId, req.getStampId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/me/stamps/{stampId}/upgrade")
    @Operation(summary = "희귀도 업그레이드", description = "레벨이 기준 미달이면 예외(400)로 메시지를 반환합니다.")
    public ResponseEntity<Void> upgrade(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Long stampId
    ) {
        Long userId = principalUser.getUser().getId();
        stampService.upgradeStampOrThrow(userId, stampId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/users/{userId}/stamps")
    @Operation(summary = "다른 사용자의 우표 목록", description = "다른 사용자가 수집한 우표들을 반환합니다.")
    public ResponseEntity<List<UserStampResponseDto>> getUserStamps(@PathVariable Long userId) {
        var list = stampService.getMyStamps(userId)
                .stream().map(UserStampResponseDto::new).toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/api/me/stamps/rarity/{rarity}")
    @Operation(summary = "희귀도별 우표 조회", description = "rarity 기준으로 내가 모은 우표를 반환합니다.")
    public ResponseEntity<List<UserStampResponseDto>> myStampsByRarity(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @PathVariable Rarity rarity
    ) {
        Long userId = principalUser.getUser().getId();
        var list = stampService.getMyStampsByRarity(userId, rarity)
                .stream().map(UserStampResponseDto::new).toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/api/me/stamps/upgrade-all")
    @Operation(summary = "희귀도 업그레이드(일괄)", description = "레벨 조건을 충족한 모든 스탬프를 업그레이드합니다.")
    public ResponseEntity<List<StampService.UpgradeResultDto>> upgradeAll(
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        Long userId = principalUser.getUser().getId();
        var results = stampService.upgradeAllEligible(userId);
        return ResponseEntity.ok(results);
    }


}
