package com.krafton.stamp.dto;

import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.Stamp;
import lombok.Getter;

@Getter
public class StampResponseDto {
    private final Long id;
    private final String name;
    private final String imageUrl;
    private final Rarity rarity;

    public StampResponseDto(Stamp stamp) {
        this.id = stamp.getId();
        this.name = stamp.getName();
        this.imageUrl = stamp.getImageUrl();
        this.rarity = stamp.getRarity();
    }
}
