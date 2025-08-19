package com.krafton.stamp.controller;

import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.service.StampLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicCatalogController {

    private final StampLookupService lookupService;

    // CORS가 필요하면 전역 CORS 설정 또는 여기서도 허용
    // @CrossOrigin(origins = "chrome-extension://<EXT_ID>")

    /** ✅ 카탈로그 내려주기: 확장프로그램이 캐시해서 사용 */
    @GetMapping("/stamp-catalog")
    public ResponseEntity<List<Item>> catalog(
            @RequestParam(name = "rarity", defaultValue = "COMMON") Rarity rarity
    ) {
        var list = lookupService.loadCatalog(rarity).stream()
                .map(p -> new Item(p.getSiteUrl(), p.getId(), p.getRarity()))
                .toList();

        // 캐싱 힌트(선택): 1시간 캐시
        return ResponseEntity.ok()
                .header("Cache-Control", "public, max-age=3600")
                .body(list);
    }

    /** ✅ 단건 조회: host 주면 서버가 규칙에 따라 stampId 찾아줌 */
    @GetMapping("/lookup-stamp")
    public ResponseEntity<LookupRes> lookup(
            @RequestParam("host") String host,
            @RequestParam(name = "rarity", defaultValue = "COMMON") Rarity rarity
    ) {
        Long id = lookupService.lookupStampId(host, rarity);
        return ResponseEntity.ok(new LookupRes(host, id, rarity, id != null));
    }

    public record Item(String domain, Long stampId, Rarity rarity) {}
    public record LookupRes(String host, Long stampId, Rarity rarity, boolean matched) {}
}
