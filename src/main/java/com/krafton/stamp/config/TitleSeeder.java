package com.krafton.stamp.config;

import com.krafton.stamp.domain.*;
import com.krafton.stamp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
//@Profile("dev")  // ❗배포 시 자동 실행되게 하려면 주석 유지
@RequiredArgsConstructor
public class TitleSeeder {

    private final TitleRepository titleRepository;

    @Bean
    ApplicationRunner seedTitles() {
        return args -> {
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

            // ✅ 프론트엔드 칭호: FRONTEND의 DISTINCT 3종 이상 보유
            upsertTitle(
                    "FRONTEND_COMMON_3DISTINCT",
                    "프론트 장인",
                    "프론트엔드 COMMON 스탬프 3종 이상 보유 시 획득",
                    Category.FRONTEND,
                    Rarity.COMMON,
                    Title.ConditionType.DISTINCT_AT_LEAST,
                    3,
                    "https://buly.kr/7mC9UNN",
                    25
            );

            // ✅ 배포 칭호: DEVOPS COMMON 스탬프 2개 이상
            upsertTitle(
                    "DEVOPS_COMMON_MINI",
                    "배포 시작자",
                    "DEVOPS 카테고리의 COMMON 스탬프 2개 이상 보유",
                    Category.DEVOPS,
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
                    null, // 전 카테고리 통합
                    Rarity.LEGENDARY,
                    Title.ConditionType.TOTAL_AT_LEAST,
                    1,
                    "https://buly.kr/3CORzBh",
                    50
            );
        };
    }

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
