package com.krafton.stamp.dto;

import com.krafton.stamp.domain.Category;
import com.krafton.stamp.domain.Rarity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class StampCreateRequestDto {

    @NotBlank
    private String name;

    @NotBlank
    private String imageUrl;

    @NotBlank
    private String siteUrl;

    @NotNull
    private Category category;

    @NotNull
    private Rarity rarity;

    private String description;
}
