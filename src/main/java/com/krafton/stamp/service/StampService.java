package com.krafton.stamp.service;

import com.krafton.stamp.domain.*;
import com.krafton.stamp.dto.StampCreateRequestDto;
import com.krafton.stamp.policy.UpgradePolicy;   // ⬅️ 추가
import com.krafton.stamp.repository.StampRepository;
import com.krafton.stamp.repository.UserRepository;
import com.krafton.stamp.repository.UserStampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StampService {

    private final UserRepository userRepository;
    private final StampRepository stampRepository;
    private final UserStampRepository userStampRepository;
    private final TitleService titleService;
    private final ScoreService scoreService;
    private final UpgradePolicy upgradePolicy;   // ⬅️ 주입

    // ===== 수집 =====
    @Transactional
    public void collectStamp(Long userId, Long stampId) {
        User user = getUserOrThrow(userId);
        Stamp stamp = getStampOrThrow(stampId);

        // 지급
        giveOne(stamp, user);

        // 점수
        scoreService.addScore(userId,
                scoreService.pointsForCollect(stamp.getRarity()),
                "COLLECT", "STAMP", stamp.getId());

        // 칭호
        titleService.evaluateAndAward(userId, stamp.getCategory(), stamp.getRarity());
    }

    // ===== 업그레이드(요청형, 한 단계만) =====
    @Transactional
    public void upgradeStampOrThrow(Long userId, Long fromStampId) {
        UserStamp userStamp = userStampRepository.findByUserIdAndStampIdForUpdate(userId, fromStampId)
                .orElseThrow(() -> new IllegalArgumentException("해당 우표를 보유하고 있지 않습니다."));

        Stamp nextStamp = doExplicitUpgradeOrThrow(userStamp); // 1스텝만

        // 칭호: 원본과 승급된 축 모두 평가
        titleService.evaluateAndAward(userId, userStamp.getStamp().getCategory(), userStamp.getStamp().getRarity());
        titleService.evaluateAndAward(userId, nextStamp.getCategory(), nextStamp.getRarity());
    }

    private Stamp doExplicitUpgradeOrThrow(UserStamp userStamp) {
        Stamp current = userStamp.getStamp();
        Rarity from = current.getRarity();
        Rarity next = nextRarity(from);
        if (next == null) throw new IllegalStateException("이미 최고 희귀도입니다.");

        int requiredLevel = upgradePolicy.requiredLevelFor(from);
        if (userStamp.getLevel() < requiredLevel)
            throw new IllegalStateException("레벨 부족: 필요 " + requiredLevel + ", 현재 " + userStamp.getLevel());

        int cost = upgradePolicy.costForUpgrade(from);
        if (userStamp.getCount() < cost)
            throw new IllegalStateException("수량 부족: 필요 " + cost + ", 보유 " + userStamp.getCount());

        Stamp nextStamp = stampRepository.findBySiteUrlAndRarity(current.getSiteUrl(), next)
                .orElseThrow(() -> new IllegalStateException("상위 희귀도 스탬프가 등록되어 있지 않습니다."));

        // 차감 + 지급
        userStamp.decreaseCount(cost);
        giveOne(nextStamp, userStamp.getUser());

        // 보너스 점수
        scoreService.addScore(userStamp.getUser().getId(),
                scoreService.bonusForUpgrade(next),
                "UPGRADE", "STAMP", nextStamp.getId());

        return nextStamp; // 1단계만
    }

    // ===== 헬퍼 =====

    private void giveOne(Stamp stamp, User user) {
        var optional = userStampRepository.findByUserIdAndStampIdForUpdate(user.getId(), stamp.getId());
        if (optional.isPresent()) {
            optional.get().increaseCount();
        } else {
            userStampRepository.save(
                    UserStamp.builder()
                            .user(user)
                            .stamp(stamp)
                            .count(1)
                            .collectedAt(LocalDateTime.now())
                            .build()
            );
        }
    }

    private Rarity nextRarity(Rarity r) {
        Rarity[] vals = Rarity.values();
        int i = r.ordinal();
        return (i + 1 < vals.length) ? vals[i + 1] : null;
    }

    // ===== 조회/생성 등 =====
    @Transactional(readOnly = true)
    public List<UserStamp> getMyStamps(Long userId) { return userStampRepository.findByUserIdWithStamp(userId); }

    @Transactional(readOnly = true)
    public List<UserStamp> getMyStampsByRarity(Long userId, Rarity rarity) { return userStampRepository.findByUserIdAndRarity(userId, rarity); }

    @Transactional(readOnly = true)
    public List<UserStamp> getMyStampsByCategory(Long userId, Category category) {
        return userStampRepository.findByUserIdAndCategory(userId, category);
    }

    @Transactional(readOnly = true)
    public int getMyCategoryCountSum(Long userId, Category category) {
        return userStampRepository.sumCountByUserAndCategory(userId, category);
    }

    @Transactional(readOnly = true)
    public List<Stamp> getStampsByCategory(Category category) { return stampRepository.findByCategory(category); }

    @Transactional(readOnly = true)
    public List<Stamp> getAllStamps() { return stampRepository.findAll(); }

    @Transactional(readOnly = true)
    public Stamp getStampDetail(Long stampId) { return getStampOrThrow(stampId); }

    @Transactional
    public void deleteStamp(Long stampId) { stampRepository.delete(getStampOrThrow(stampId)); }

    @Transactional
    public Stamp createStamp(StampCreateRequestDto req) {
        if (stampRepository.existsBySiteUrlAndRarity(req.getSiteUrl(), req.getRarity())) {
            throw new IllegalArgumentException("이미 해당 사이트/희귀도의 우표가 존재합니다.");
        }
        Stamp stamp = Stamp.builder()
                .name(req.getName())
                .imageUrl(req.getImageUrl())
                .siteUrl(req.getSiteUrl())
                .category(req.getCategory())
                .rarity(req.getRarity())
                .description(req.getDescription())
                .build();
        return stampRepository.save(stamp);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
    private Stamp getStampOrThrow(Long stampId) {
        return stampRepository.findById(stampId)
                .orElseThrow(() -> new IllegalArgumentException("우표를 찾을 수 없습니다."));
    }

    public static record UpgradeResultDto(
            Long fromStampId,
            Long toStampId,
            boolean upgraded,
            int costConsumed,
            String message
    ) {}

    /** 배치 업그레이드: 각 스탬프에 대해 '한 단계만' 시도 */
    @Transactional
    public List<UpgradeResultDto> upgradeAllEligible(Long userId) {
        var list = userStampRepository.findByUserIdWithStamp(userId);
        var results = new java.util.ArrayList<UpgradeResultDto>();

        for (var us0 : list) {
            var lockedOpt = userStampRepository.findByUserIdAndStampIdForUpdate(userId, us0.getStamp().getId());
            if (lockedOpt.isEmpty()) {
                results.add(new UpgradeResultDto(us0.getStamp().getId(), null, false, 0, "보유 기록이 없습니다."));
                continue;
            }
            var us = lockedOpt.get();
            var current = us.getStamp();
            var next = nextRarity(current.getRarity());
            if (next == null) {
                results.add(new UpgradeResultDto(current.getId(), null, false, 0, "이미 최고 희귀도"));
                continue;
            }

            if (!upgradePolicy.canUpgrade(us)) {
                int needLv = upgradePolicy.requiredLevelFor(current.getRarity());
                int needCnt = upgradePolicy.costForUpgrade(current.getRarity());
                results.add(new UpgradeResultDto(current.getId(), null, false, 0,
                        "조건 미충족 (필요Lv=" + needLv + ", 필요수량=" + needCnt + ", 현재Lv=" + us.getLevel() + ", 보유=" + us.getCount() + ")"));
                continue;
            }

            var nextOpt = stampRepository.findBySiteUrlAndRarity(current.getSiteUrl(), next);
            if (nextOpt.isEmpty()) {
                results.add(new UpgradeResultDto(current.getId(), null, false, 0, "상위 희귀도 스탬프 없음"));
                continue;
            }

            int cost = upgradePolicy.costForUpgrade(current.getRarity());
            us.decreaseCount(cost);
            giveOne(nextOpt.get(), us.getUser());

            // 보너스 점수 & 칭호 (한 단계만 수행)
            scoreService.addScore(us.getUser().getId(),
                    scoreService.bonusForUpgrade(nextOpt.get().getRarity()),
                    "UPGRADE", "STAMP", nextOpt.get().getId());

            titleService.evaluateAndAward(userId, us.getStamp().getCategory(), us.getStamp().getRarity());
            titleService.evaluateAndAward(userId, nextOpt.get().getCategory(), nextOpt.get().getRarity());

            results.add(new UpgradeResultDto(current.getId(), nextOpt.get().getId(), true, cost, "OK"));
        }

        return results;
    }

    @Transactional
    public void rewardStampByMission(Long userId, String siteUrl, Rarity rarity) {
        User user = getUserOrThrow(userId);
        Stamp stamp = stampRepository.findBySiteUrlAndRarity(siteUrl, rarity)
                .orElseThrow(() -> new IllegalStateException("해당 사이트/희귀도의 우표가 등록되어 있지 않습니다."));
        giveOne(stamp, user);

        scoreService.addScore(userId,
                scoreService.pointsForCollect(stamp.getRarity()),
                "COLLECT", "STAMP", stamp.getId());

        titleService.evaluateAndAward(userId, stamp.getCategory(), stamp.getRarity());
    }

    @Transactional
    public boolean tryRewardStampByMission(Long userId, String siteUrl, Rarity rarity) {
        User user = getUserOrThrow(userId);
        return stampRepository.findBySiteUrlAndRarity(siteUrl, rarity)
                .map(stamp -> { giveOne(stamp, user); return true; })
                .orElse(false);
    }
}
