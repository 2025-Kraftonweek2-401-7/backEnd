package com.krafton.stamp.config;

import com.krafton.stamp.domain.Category;
import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.Title;
import com.krafton.stamp.repository.TitleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Configuration
//@Profile("dev")  // ❗배포 시 자동 실행되게 하려면 주석 유지
@RequiredArgsConstructor
public class TitleSeeder {

    private final TitleRepository titleRepository;

    /** 레어도별 포인트 */
    private int pointForRarity(Rarity r) {
        return switch (r) {
            case COMMON -> 40;
            case RARE -> 60;
            case EPIC -> 80;
            case LEGENDARY -> 120;
        };
    }

    /** 공용 upsert */
    private void upsertTitle(
            String code, String name, String desc,
            Category category, Rarity rarity,
            Title.ConditionType type, Integer requiredCount,
            String imageUrl, Integer pointReward
    ) {
        boolean exists = titleRepository.findAll().stream()
                .anyMatch(t -> t.getCode().equals(code));
        if (exists) return;

        titleRepository.save(
                Title.builder()
                        .code(code)
                        .name(name)
                        .description(desc)
                        .category(category)           // 전체 카테고리용이면 null 가능 (엔티티/DDL에서 null 허용이어야 함)
                        .rarity(rarity)
                        .conditionType(type)
                        .requiredCount(requiredCount) // COMPLETE_SET면 null
                        .imageUrl(imageUrl)
                        .pointReward(pointReward)
                        .active(true)
                        .build()
        );
    }

    // --- 1) 풀세트(카테고리×레어도) 생성 로직 교체 ---
    private void seedCategoryRarityFullSets() {
        Set<String> existing = titleRepository.findAll().stream()
                .map(Title::getCode)
                .collect(Collectors.toSet());

        List<Category> categories = List.of(
                Category.BACKEND, Category.FRONTEND, Category.AI, Category.LEARNING, Category.TOOL, Category.DEVOPS
        );
        List<Rarity> rarities = List.of(
                Rarity.COMMON, Rarity.RARE, Rarity.EPIC, Rarity.LEGENDARY
        );

        int created = 0;
        for (Category c : categories) {
            for (Rarity r : rarities) {
                String code = "%s_%s_MASTER".formatted(c.name(), r.name());      // ✅ CODE 규칙
                if (existing.contains(code)) continue;

                titleRepository.save(
                        Title.builder()
                                .code(code)
                                .name("%s %s MASTER".formatted(c.name(), r.name())) // ✅ NAME 규칙(ALL CAPS)
                                .description("%s 카테고리의 %s 스탬프를 모두 수집하면 획득합니다."
                                        .formatted(c.name(), r.name()))
                                .category(c)
                                .rarity(r)
                                .conditionType(Title.ConditionType.COMPLETE_SET)      // ✅ 전부 모았을 때
                                .requiredCount(null)                                   // ✅ 세트완성: 개수 미지정
                                .imageUrl("/assets/titles/%s-%s-master.png"
                                        .formatted(c.name().toLowerCase(), r.name().toLowerCase()))
                                .pointReward(pointForRarity(r))
                                .active(true)
                                .build()
                );
                created++;
            }
        }
        log.info("✅ Category×Rarity MASTER titles seeded: {}", created);
    }

    @Bean
    ApplicationRunner seedTitles() {
        return args -> {
            // -----------------------------
            // 1) 기존 개수/종수 기준 칭호
            // -----------------------------

            // ✅ 백엔드 칭호: BACKEND의 COMMON 5개 이상 수집
            upsertTitle(
                    "BACKEND_COMMON_COLLECTOR",
                    "백엔드 수집가",
                    "백엔드 COMMON 스탬프를 5개 이상 모아야 획득할 수 있는 칭호",
                    Category.BACKEND,
                    Rarity.COMMON,
                    Title.ConditionType.TOTAL_AT_LEAST,
                    5,
                    "https://img/title-backend-collector.png",
                    30
            );

            // ✅ 프론트엔드 칭호: FRONTEND의 DISTINCT 2종 이상 보유
            upsertTitle(
                    "FRONTEND_COMMON_3DISTINCT",
                    "프론트 장인",
                    "프론트엔드 COMMON 스탬프 2종 이상 보유 시 획득",
                    Category.FRONTEND,
                    Rarity.COMMON,
                    Title.ConditionType.DISTINCT_AT_LEAST,
                    2,
                    "https://buly.kr/7mC9UNN",
                    25
            );

            // ✅ 배포(툴) 칭호: TOOL COMMON 스탬프 2개 이상
            upsertTitle(
                    "DEVOPS_COMMON_MINI",
                    "배포 시작자",
                    "DEVOPS 카테고리의 COMMON 스탬프 2개 이상 보유",
                    Category.DEVOPS,                  // ✅ DEVOPS 유지
                    Rarity.COMMON,
                    Title.ConditionType.TOTAL_AT_LEAST,
                    2,
                    "https://buly.kr/AF0lDoE",
                    20
            );

            // ✅ 학습 칭호: LEARNING의 COMMON 3종 보유
            upsertTitle(
                    "LEARNING_COMMON_3DISTINCT",
                    "꾸준한 학습가",
                    "LEARNING 카테고리의 COMMON 스탬프 3종 이상 보유",
                    Category.LEARNING,
                    Rarity.COMMON,
                    Title.ConditionType.DISTINCT_AT_LEAST,
                    3,
                    "https://buly.kr/AaqHBT9",
                    25
            );

            // ✅ 레전더리 칭호: 어떤 카테고리든 LEGENDARY 스탬프 1개라도 있으면 부여
            upsertTitle(
                    "LEGENDARY_COLLECTOR",
                    "스탬프 전설",
                    "어떤 카테고리든 LEGENDARY 스탬프를 수집한 유저에게 수여",
                    null, // 전 카테고리 통합(엔티티/DDL에서 null 허용인지 확인)
                    Rarity.LEGENDARY,
                    Title.ConditionType.TOTAL_AT_LEAST,
                    1,
                    "https://buly.kr/3CORzBh",
                    50
            );

            // ✅ AI 칭호: AI 카테고리 COMMON 1개 이상 보유
            upsertTitle(
                    "AI_COMMON_COLLECTOR",
                    "AI 개척자",
                    "AI 카테고리의 COMMON 스탬프를 1개 이상 보유 시 획득",
                    Category.AI,
                    Rarity.COMMON,
                    Title.ConditionType.TOTAL_AT_LEAST,
                    1,
                    "https://buly.kr/ai-pioneer",
                    20
            );

            // ✅ 도구(TOOL) 칭호: TOOL 카테고리 COMMON 2개 이상 보유
            upsertTitle(
                    "TOOL_COMMON_MINI_DUP",
                    "툴 숙련자",
                    "TOOL 카테고리의 COMMON 스탬프 2개 이상 보유",
                    Category.TOOL,
                    Rarity.COMMON,
                    Title.ConditionType.TOTAL_AT_LEAST,
                    2,
                    "https://buly.kr/tools-mini",
                    20
            );

            // -----------------------------
            // 2) 카테고리×레어도 풀세트 칭호 생성
            // -----------------------------
            seedCategoryRarityFullSets();
        };
    }
}
