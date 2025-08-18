package com.krafton.stamp.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "title")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Title {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String code;             // 예: BACKEND_COMMON_COMPLETE

    @Column(nullable=false)
    private String name;             // 예: 백엔드 입문자

    private String imageUrl;         // 대표뱃지 이미지

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;       // 필터(권장)

    @Enumerated(EnumType.STRING)
    private Rarity rarity;           // 필터(권장)

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private ConditionType conditionType;  // COMPLETE_SET, DISTINCT_AT_LEAST, TOTAL_AT_LEAST

    private Integer requiredCount;        // COMPLETE_SET이면 null 가능

    private boolean active = true;

    public enum ConditionType {
        COMPLETE_SET, DISTINCT_AT_LEAST, TOTAL_AT_LEAST
    }

    private Integer pointReward;
}
