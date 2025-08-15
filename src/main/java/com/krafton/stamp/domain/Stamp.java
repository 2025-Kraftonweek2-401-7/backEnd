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
    private String siteUrl; //ex : gitHub

    @Enumerated(EnumType.STRING) // ✅ Enum을 문자열로 저장
    private Rarity rarity;

    private String description;


    @Builder
    public Stamp(String name, String imageUrl, String siteUrl, Rarity rarity, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.siteUrl = siteUrl;
        this.rarity = rarity;
        this.description = description;
    }
}

