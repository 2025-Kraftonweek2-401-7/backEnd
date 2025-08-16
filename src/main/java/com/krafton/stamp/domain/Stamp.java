package com.krafton.stamp.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Stamp {

    @Id @GeneratedValue
    private Long id;

    private String name;
    private String imageUrl;
    private String siteUrl;

    @Enumerated(EnumType.STRING)
    private Rarity rarity;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) // ❗ 필수로 할 경우
    private Category category;  // ✅ 카테고리 필드 추가

    @Builder
    public Stamp(String name, String imageUrl, String siteUrl,
                 Rarity rarity, String description, Category category) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.siteUrl = siteUrl;
        this.rarity = rarity;
        this.description = description;
        this.category = category;
    }
}


