package com.krafton.stamp.controller;

import com.krafton.stamp.domain.Category;
import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.Title;
import com.krafton.stamp.repository.TitleRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/titles")
public class PublicTitleController {

    private final TitleRepository titleRepository;

    /**
     * 활성 타이틀 전체 조회 (옵션 필터: category, rarity)
     * 예 /api/public/titles
     *     /api/public/titles?category=BACKEND
     *     /api/public/titles?rarity=COMMON
     *     /api/public/titles?category=BACKEND&rarity=COMMON
     */
    @GetMapping
    @Operation(summary = "활성 타이틀 전체 조회(필터 가능)")
    public ResponseEntity<List<TitleDto>> list(
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Rarity rarity
    ) {
        var titles = titleRepository.findActiveByOptionalFilters(category, rarity);
        var body = titles.stream().map(TitleDto::from).toList();

        return ResponseEntity.ok()
                .header("Cache-Control", "public, max-age=300")
                .body(body);
    }

    public record TitleDto(
            Long id,
            String name,
            Category category,
            Rarity rarity,
            String description,
            String imageUrl
    ) {
        public static TitleDto from(Title t) {
            return new TitleDto(
                    t.getId(),
                    t.getName(),
                    t.getCategory(),
                    t.getRarity(),
                    t.getDescription(), // null 가능
                    t.getImageUrl()      // null 가능
            );
        }
    }
}
