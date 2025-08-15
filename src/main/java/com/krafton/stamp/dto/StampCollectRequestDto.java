package com.krafton.stamp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StampCollectRequestDto {
    private Long userId;
    private Long stampId;
}
