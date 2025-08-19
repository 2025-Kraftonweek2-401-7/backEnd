package com.krafton.stamp.policy;

import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.UserStamp;
import org.springframework.stereotype.Component;

@Component
public class UpgradePolicy {

    /** 다음 희귀도로 승급하기 위한 '필요 레벨' */
    public int requiredLevelFor(Rarity from) {
        return switch (from) {
            case LEGENDARY -> Integer.MAX_VALUE; // 다음 없음
            default -> 2; // COMMON, RARE, EPIC 모두 레벨2면 승급 가능
        };
    }

    /** 다음 희귀도로 승급하기 위한 '소모 개수' */
    public int costForUpgrade(Rarity from) {
        return switch (from) {
            case COMMON -> 5; // COMMON → RARE
            case RARE   -> 5; // RARE   → EPIC
            case EPIC   -> 5; // EPIC   → LEGENDARY
            case LEGENDARY -> Integer.MAX_VALUE;
        };
    }

    /** 현재 보유 상태로 승급 가능 여부(한 단계만 판단) */
    public boolean canUpgrade(UserStamp us) {
        int requiredLevel = requiredLevelFor(us.getStamp().getRarity());
        int cost = costForUpgrade(us.getStamp().getRarity());
        return us.getLevel() >= requiredLevel && us.getCount() >= cost;
    }
}
