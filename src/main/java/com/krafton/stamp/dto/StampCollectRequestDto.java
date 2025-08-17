package com.krafton.stamp.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StampCollectRequestDto {
    @NotNull
    private Long stampId;
}
