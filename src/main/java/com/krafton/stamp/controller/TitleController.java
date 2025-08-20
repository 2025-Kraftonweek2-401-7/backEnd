package com.krafton.stamp.controller;

import com.krafton.stamp.domain.*;
import com.krafton.stamp.dto.UserTitleResponseDto;
import com.krafton.stamp.repository.StampRepository;
import com.krafton.stamp.security.PrincipalUser;
import com.krafton.stamp.service.TitleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/me/titles")
public class TitleController {

    private final TitleService titleService;
    private final StampRepository stampRepository;

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
        var rep = titleService.getRepresentative(userId).orElseThrow();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/representative")
    @Operation(summary = "내 대표 칭호 조회", description = "대표 칭호가 없으면 204 No Content")
    public ResponseEntity<UserTitleResponseDto> myRepresentative(@AuthenticationPrincipal PrincipalUser principal) {
        Long userId = principal.getUser().getId();
        return titleService.getRepresentative(userId)
                .map(ut -> ResponseEntity.ok(new UserTitleResponseDto(ut)))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PostMapping("/evaluate-by-stamp")
    @Operation(summary = "스탬프 ID로 칭호 평가·지급",
            description = "stampId에서 category/rarity를 찾아 해당 축만 평가하고, 이번에 새로 지급된 칭호만 반환")
    public ResponseEntity<List<TitleDto>> evaluateByStamp(
            @AuthenticationPrincipal PrincipalUser principal,
            @RequestParam Long stampId
    ) {
        Long userId = principal.getUser().getId();
        Stamp s = stampRepository.findById(stampId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Stamp not found"));

        var newly = titleService.evaluateAndAward(userId, s.getCategory(), s.getRarity())
                .stream().map(TitleDto::new).toList();

        return ResponseEntity.ok(newly);
    }

    // 필요 필드만 담은 경량 DTO
    public record TitleDto(
            Long id,
            String code,
            String name,
            String imageUrl,
            @com.fasterxml.jackson.annotation.JsonProperty("score") Integer pointReward // JSON 키를 score로 노출하고 싶다면
    ) {
        public TitleDto(Title t) {
            this(t.getId(), t.getCode(), t.getName(), t.getImageUrl(), t.getPointReward());
        }
    }


}
