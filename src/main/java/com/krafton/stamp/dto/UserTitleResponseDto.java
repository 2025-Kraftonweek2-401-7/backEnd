package com.krafton.stamp.dto;

import com.krafton.stamp.domain.Category;
import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.UserTitle;

import java.time.LocalDateTime;

public record UserTitleResponseDto(
        Long userTitleId,
        Long titleId,
        String code,
        String name,
        String imageUrl,
        String description,
        Category category,
        Rarity rarity,
        boolean representative,
        LocalDateTime acquiredAt
) {
    public UserTitleResponseDto(UserTitle ut) {
        this(
                ut.getId(),
                ut.getTitle().getId(),
                ut.getTitle().getCode(),
                ut.getTitle().getName(),
                ut.getTitle().getImageUrl(),
                ut.getTitle().getDescription(),
                ut.getTitle().getCategory(),
                ut.getTitle().getRarity(),
                ut.isRepresentative(),
                ut.getAcquiredAt()
        );
    }
}
