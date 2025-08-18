package com.krafton.stamp.controller;

import com.krafton.stamp.domain.*;
import com.krafton.stamp.dto.UserTitleResponseDto;
import com.krafton.stamp.security.PrincipalUser;
import com.krafton.stamp.service.TitleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me/titles")
public class TitleController {

    private final TitleService titleService;

    @GetMapping
    @Operation(summary = "내 칭호 목록")
    public ResponseEntity<List<UserTitleResponseDto>> myTitles(@AuthenticationPrincipal PrincipalUser principal) {
        Long userId = principal.getUser().getId();
        var list = titleService.getMyTitles(userId)
                .stream().map(UserTitleResponseDto::new).toList();
        return ResponseEntity.ok(list);
    }


    @PostMapping("/evaluate")
    @Operation(summary = "칭호 평가·지급", description = "최근 변경된 카테고리/등급을 전달해 해당 축의 칭호만 평가")
    public ResponseEntity<List<Title>> evaluate(
            @AuthenticationPrincipal PrincipalUser principal,
            @RequestParam Category category,
            @RequestParam Rarity rarity
    ) {
        Long userId = principal.getUser().getId();
        return ResponseEntity.ok(titleService.evaluateAndAward(userId, category, rarity));
    }

    @PostMapping("/{titleId}/represent")
    @Operation(summary = "대표 뱃지 설정")
    public ResponseEntity<Void> setRepresentative(
            @AuthenticationPrincipal PrincipalUser principal,
            @PathVariable Long titleId
    ) {
        Long userId = principal.getUser().getId();
        titleService.setRepresentative(userId, titleId);
        return ResponseEntity.ok().build();
    }
}
