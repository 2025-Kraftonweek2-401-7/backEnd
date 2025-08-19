package com.krafton.stamp.repository;

import com.krafton.stamp.domain.Rarity;

// ✅ 도메인/아이디/희귀도만 가볍게 가져오도록 Projection
public interface StampDomainProjection {
    Long getId();
    String getSiteUrl();   // 예: "github.com" 혹은 "*.stackoverflow.com"
    Rarity getRarity();
}
