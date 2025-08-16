package com.krafton.stamp.dto;

import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.Stamp;
import lombok.Getter;
import com.krafton.stamp.domain.Category;

@Getter
public class StampResponseDto {
    private final Long id;
    private final String name;
    private final String imageUrl;
    private final Rarity rarity;
    private final Category category; // ✅ 추가
    private final String siteUrl;    // ✅ 추가 (도감용 정보라면 유용)

    public StampResponseDto(Stamp stamp) {
        this.id = stamp.getId();
        this.name = stamp.getName();
        this.imageUrl = stamp.getImageUrl();
        this.rarity = stamp.getRarity();
        this.category = stamp.getCategory(); // ✅
        this.siteUrl = stamp.getSiteUrl();   // ✅
    }
}
