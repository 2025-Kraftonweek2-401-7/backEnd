package com.krafton.stamp.controller;

import com.krafton.stamp.domain.Category;
import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.Stamp;
import com.krafton.stamp.repository.StampRepository;
import com.krafton.stamp.service.StampLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicCatalogController {

    private final StampLookupService lookupService; // 기존 그대로 사용
    private final StampRepository stampRepository;  // ⬅️ 추가

    /**
     * ✅ 카탈로그 내려주기: 확장프로그램이 캐시해서 사용
     */
    @GetMapping("/stamp-catalog")
    public ResponseEntity<List<Item>> catalog(
            @RequestParam(name = "rarity", defaultValue = "COMMON") Rarity rarity
    ) {
        var list = lookupService.loadCatalog(rarity).stream()
                .map(p -> new Item(p.getSiteUrl(), p.getId(), p.getRarity()))
                .toList();

        return ResponseEntity.ok()
                .header("Cache-Control", "public, max-age=3600")
                .body(list);
    }

    /**
     * ✅ 단건 조회: host -> stampId 매칭 + 이름/이미지/카테고리 포함
     */
    @GetMapping("/lookup-stamp")
    public ResponseEntity<LookupRes> lookup(
            @RequestParam("host") String host,
            @RequestParam(name = "rarity", defaultValue = "COMMON") Rarity rarity
    ) {
        Long id = lookupService.lookupStampId(host, rarity);

        if (id == null) {
            return ResponseEntity.ok(new LookupRes(
                    host, null, rarity, false, null, null, null, null
            ));
        }

        // ⬇️ stamp 한번 더 조회해서 이름/이미지/카테고리 채움
        Stamp stamp = stampRepository.findById(id).orElse(null);
        if (stamp == null) {
            // id는 찾았는데 레코드가 없으면 matched=false로 안전 처리
            return ResponseEntity.ok(new LookupRes(
                    host, id, rarity, false, null, null, null, null
            ));
        }

        return ResponseEntity.ok(new LookupRes(
                host,
                stamp.getId(),
                stamp.getRarity(),     // 응답은 실제 스탬프 등급으로 덮어쓰기 (필요 없으면 rarity 사용)
                true,
                stamp.getName(),
                stamp.getImageUrl(),
                stamp.getImageUrl(),   // thumb128 별도 없으면 임시로 동일 값
                stamp.getCategory()    // ✅ 추가
        ));
    }

    /**
     * 필요하면 내부 캐시 아이템
     */
    public record Item(String domain, Long stampId, Rarity rarity) {
    }

    /**
     * ✅ 응답 레코드에 category 추가
     */
    public record LookupRes(
            String host,
            Long stampId,
            Rarity rarity,
            boolean matched,
            String stampName,
            String imageUrl,
            String thumb128,
            Category category     // ✅ 추가 필드
    ) {
    }


}
