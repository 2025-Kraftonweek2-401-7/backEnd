package com.krafton.stamp.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "mission",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_mission_site", columnNames = {"site_url"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Mission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type", nullable = false)
    @Builder.Default
    private ConditionType conditionType = ConditionType.VISIT_COUNT; // 초기엔 고정

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Rarity rarity = Rarity.COMMON;

    @Column(name = "target_value", nullable = false)
    private Integer targetValue;   // 예: 5 (방문 5회면 달성)

    @Column(name = "site_url", nullable = false, length = 255)
    private String siteUrl;
}
