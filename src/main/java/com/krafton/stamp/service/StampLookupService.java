package com.krafton.stamp.service;

import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.repository.StampDomainProjection;
import com.krafton.stamp.repository.StampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StampLookupService {

    private final StampRepository stampRepository;

    /** 호스트 정규화: 소문자 + www. 제거 */
    public static String normalizeHost(String host) {
        if (host == null) return null;
        String h = host.toLowerCase();
        return h.startsWith("www.") ? h.substring(4) : h;
    }

    /** 카탈로그(희귀도별) 로드 */
    @Transactional(readOnly = true)
    public List<StampDomainProjection> loadCatalog(Rarity rarity) {
        return stampRepository.findByRarity(rarity);
    }

    /** 단건 조회: host와 rarity로 stampId 찾기 (정확히→와일드카드→suffix) */
    @Transactional(readOnly = true)
    public Long lookupStampId(String rawHost, Rarity rarity) {
        String host = normalizeHost(rawHost);
        if (host == null || host.isBlank()) return null;

        // 1) 정확히 일치 (예: "github.com")
        var exact = stampRepository.findBySiteUrlAndRarity(host, rarity);
        if (exact.isPresent()) return exact.get().getId();

        // 2) 와일드카드 매칭 (예: "*.stackoverflow.com")
        var all = stampRepository.findByRarity(rarity);
        for (var p : all) {
            String site = p.getSiteUrl().toLowerCase();
            if (site.startsWith("*.")) {
                String suffix = site.substring(1); // ".stackoverflow.com"
                if (host.endsWith(suffix)) return p.getId();
            }
        }

        // 3) suffix 매칭 허용 (선택): DB에 "example.com" 있고 방문은 "sub.example.com"
        for (var p : all) {
            String site = p.getSiteUrl().toLowerCase();
            if (!site.startsWith("*.") && host.endsWith("." + site)) {
                return p.getId();
            }
        }

        return null;
    }
}
