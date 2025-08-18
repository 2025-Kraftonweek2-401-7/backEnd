package com.krafton.stamp.config;

import com.krafton.stamp.domain.*;
import com.krafton.stamp.repository.*;
import com.krafton.stamp.service.StampService;
import com.krafton.stamp.service.TitleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TitleSeeder {

    private final UserRepository userRepository;
    private final StampRepository stampRepository;
    private final TitleRepository titleRepository;
    private final UserTitleRepository userTitleRepository;
    private final StampService stampService;
    private final TitleService titleService;

    @Bean
    ApplicationRunner seedTitlesAndTest() {
        return args -> {
            // 1) 테스트 유저 준비
            User user = userRepository.findByEmail("tester@example.com")
                    .orElseGet(() -> userRepository.save(
                            User.builder()
                                    .username("tester")
                                    .email("tester@example.com")
                                    .provider("local")
                                    .build()
                    ));
            Long userId = user.getId();

            // 2) 스탬프 준비 (BACKEND / COMMON 3개 예시)
            List<Stamp> backendCommon = List.of(
                    stamp("백엔드 A", "https://img/a.png", "https://site/a", Category.BACKEND, Rarity.COMMON, "A"),
                    stamp("백엔드 B", "https://img/b.png", "https://site/b", Category.BACKEND, Rarity.COMMON, "B"),
                    stamp("백엔드 C", "https://img/c.png", "https://site/c", Category.BACKEND, Rarity.COMMON, "C")
            );

            for (Stamp s : backendCommon) {
                if (!stampRepository.existsBySiteUrlAndRarity(s.getSiteUrl(), s.getRarity())) {
                    stampRepository.save(s);
                }
            }
            // DB에 저장된 동일 조건의 스탬프 다시 조회
            var savedBackendCommon = stampRepository.findByCategory(Category.BACKEND)
                    .stream().filter(s -> s.getRarity() == Rarity.COMMON).toList();

            // 3) 타이틀 준비
            // (1) 백엔드 COMMON "세트 완성" → 모든 BACKEND/COMMON 스탬프 보유 시
            upsertTitle(
                    "BACKEND_COMMON_COMPLETE",
                    "백엔드 입문자",
                    "백엔드 COMMON 등급을 전부 모으면 획득",
                    Category.BACKEND,
                    Rarity.COMMON,
                    Title.ConditionType.COMPLETE_SET,
                    null,
                    "https://img/title-backend-beginner.png",
                    50
            );

            // (2) 백엔드 COMMON "서로 다른 2종 이상 보유"
            upsertTitle(
                    "BACKEND_COMMON_2DISTINCT",
                    "백엔드 아마추어",
                    "백엔드 COMMON을 서로 다른 2종 이상 보유",
                    Category.BACKEND,
                    Rarity.COMMON,
                    Title.ConditionType.DISTINCT_AT_LEAST,
                    2,
                    "https://img/title-backend-amateur.png",
                    20
            );

            // (3) 백엔드 COMMON "총 수량 5개 이상" (중복 포함)
            upsertTitle(
                    "BACKEND_COMMON_TOTAL5",
                    "백엔드 수집가",
                    "백엔드 COMMON 총 수량 5개 이상",
                    Category.BACKEND,
                    Rarity.COMMON,
                    Title.ConditionType.TOTAL_AT_LEAST,
                    5,
                    "https://img/title-backend-collector.png",
                    30
            );

            // 4) 수집 시나리오 실행 (수집마다 TitleService가 자동 평가됨)
            // tester에게 BACKEND/COMMON 3종 모두 2개씩 주기 → DISTINCT 2종, TOTAL 5개, COMPLETE_SET 모두 충족
            for (Stamp s : savedBackendCommon) {
                // 동일 스탬프 2회 수집 → count=2
                stampService.collectStamp(userId, s.getId());
                stampService.collectStamp(userId, s.getId());
            }

            // 5) 결과 확인 로그
            var titles = titleService.getMyTitles(userId);
            log.info("획득 칭호 수: {}", titles.size());
            titles.forEach(ut ->
                    log.info("획득 칭호: {} ({}) 대표여부={}",
                            ut.getTitle().getName(), ut.getTitle().getCode(), ut.isRepresentative())
            );
        };
    }

    private Stamp stamp(String name, String image, String site, Category category, Rarity rarity, String desc) {
        return Stamp.builder()
                .name(name)
                .imageUrl(image)
                .siteUrl(site)
                .category(category)
                .rarity(rarity)
                .description(desc)
                .build();
    }

    private void upsertTitle(
            String code, String name, String desc,
            Category category, Rarity rarity,
            Title.ConditionType type, Integer requiredCount,
            String imageUrl, Integer pointReward
    ) {
        var existing = titleRepository.findAll().stream()
                .filter(t -> t.getCode().equals(code)).findFirst();
        if (existing.isPresent()) return;

        titleRepository.save(
                Title.builder()
                        .code(code)
                        .name(name)
                        .description(desc)
                        .category(category)
                        .rarity(rarity)
                        .conditionType(type)
                        .requiredCount(requiredCount)
                        .imageUrl(imageUrl)
                        .pointReward(pointReward)
                        .active(true)
                        .build()
        );
    }
}
