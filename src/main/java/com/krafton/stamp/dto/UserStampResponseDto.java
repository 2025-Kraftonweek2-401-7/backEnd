package com.krafton.stamp.dto;

import com.krafton.stamp.domain.Category;
import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.UserStamp;
import lombok.Getter;

@Getter
public class UserStampResponseDto {

    private final Long stampId;
    private final String stampName;
    private final String imageUrl;
    private Rarity rarity;
    private final int count;
    private final int level;
    private final Category category;

    public UserStampResponseDto(UserStamp userStamp) {
        this.stampId = userStamp.getStamp().getId();
        this.stampName = userStamp.getStamp().getName();
        this.imageUrl = userStamp.getStamp().getImageUrl();
        this.rarity = userStamp.getStamp().getRarity();
        this.count = userStamp.getCount();
        this.level = userStamp.getLevel();
        this.category = userStamp.getStamp().getCategory();
    }
}
