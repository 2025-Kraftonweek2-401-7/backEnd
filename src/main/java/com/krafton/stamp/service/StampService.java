package com.krafton.stamp.service;

import com.krafton.stamp.domain.*;
import com.krafton.stamp.dto.StampCreateRequestDto;
import com.krafton.stamp.repository.StampRepository;
import com.krafton.stamp.repository.UserRepository;
import com.krafton.stamp.repository.UserStampRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StampService {

    private final UserRepository userRepository;
    private final StampRepository stampRepository;
    private final UserStampRepository userStampRepository;

    // ===== 수집 =====
    @Transactional
    public void collectStamp(Long userId, Long stampId) {
        User user = getUserOrThrow(userId);
        Stamp stamp = getStampOrThrow(stampId);

        var optional = userStampRepository.findByUserIdAndStampIdForUpdate(user.getId(), stamp.getId());
        if (optional.isPresent()) {
            optional.get().increaseCount();   // 레벨은 엔티티 내부에서 재계산
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

        // ❌ 자동 업그레이드 비활성화 (요청 시에만 수행)
        // tryUpgradeIfEligible(userStamp);  <-- 제거
    }

    // ===== 업그레이드(요청형) =====
    @Transactional
    public void upgradeStampOrThrow(Long userId, Long fromStampId) {
        UserStamp userStamp = userStampRepository.findByUserIdAndStampIdForUpdate(userId, fromStampId)
                .orElseThrow(() -> new IllegalArgumentException("해당 우표를 보유하고 있지 않습니다."));

        doExplicitUpgradeOrThrow(userStamp);
    }

    /**
     * 요청형 업그레이드:
     * - 현재 희귀도의 '필요 레벨'을 만족해야 함
     * - 현재 희귀도의 '소모 개수'를 원본 count에서 차감
     * - 상위 희귀도 스탬프를 1개 지급
     * - 원본은 삭제하지 않음(남은 개수 유지)
     */
    private void doExplicitUpgradeOrThrow(UserStamp userStamp) {
        Stamp current = userStamp.getStamp();
        Rarity from = current.getRarity();
        Rarity next = nextRarity(from);
        if (next == null) {
            throw new IllegalStateException("이미 최고 희귀도입니다.");
        }

        int requiredLevel = requiredLevelFor(from);
        if (userStamp.getLevel() < requiredLevel) {
            throw new IllegalStateException("아직 개수가 부족합니다. (필요 레벨: "
                    + requiredLevel + ", 현재 레벨: " + userStamp.getLevel() + ")");
        }

        int cost = costForUpgrade(from); // ← 여기서 소모량 정의 (예: COMMON→RARE 는 10개)
        if (userStamp.getCount() < cost) {
            throw new IllegalStateException("업그레이드에 필요한 수량이 부족합니다. (필요: "
                    + cost + ", 보유: " + userStamp.getCount() + ")");
        }

        // 상위 희귀도 스탬프 존재 확인
        Stamp nextStamp = stampRepository.findBySiteUrlAndRarity(current.getSiteUrl(), next)
                .orElseThrow(() -> new IllegalStateException("상위 희귀도 스탬프가 등록되어 있지 않습니다."));

        // 1) 원본에서 'cost' 만큼 차감 (남은 개수 유지)
        userStamp.decreaseCount(cost);

        // 2) 상위 희귀도 1개 지급 (이미 있으면 count++)
        giveOne(nextStamp, userStamp.getUser());
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

    // 희귀도 진행 (enum 순서 기준)
    private Rarity nextRarity(Rarity r) {
        Rarity[] vals = Rarity.values();
        int i = r.ordinal();
        return (i + 1 < vals.length) ? vals[i + 1] : null;
    }

    /** 업그레이드 '레벨' 요구치 — 정책 숫자만 바꾸면 됨 */
    private int requiredLevelFor(Rarity from) {
        // 현재 enum: COMMON, RARE, LEGENDARY
        return switch (from) {
            case COMMON -> 2; // Lv2 이상이면 RARE로
            case RARE -> 4;   // Lv4 이상이면 LEGENDARY로
            case LEGENDARY -> Integer.MAX_VALUE; // 다음 없음
        };
    }

    /** 업그레이드 '소모 개수' — 여기 정책으로 10개 소모 등을 통제 */
    private int costForUpgrade(Rarity from) {
        return switch (from) {
            case COMMON -> 10; // 예: 13개 보유 후 업그레이드 시 10개 소모 → 3개 남김
            case RARE -> 25;   // 예시값(원하면 10 등으로 변경)
            case LEGENDARY -> Integer.MAX_VALUE;
        };
    }

    // ===== 조회/생성 등 나머지는 기존 그대로 =====
    @Transactional(readOnly = true)
    public List<UserStamp> getMyStamps(Long userId) { return userStampRepository.findByUserIdWithStamp(userId); }

    @Transactional(readOnly = true)
    public List<UserStamp> getMyStampsByRarity(Long userId, Rarity rarity) { return userStampRepository.findByUserIdAndRarity(userId, rarity); }

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
        // 중복 검사: (siteUrl, rarity)
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

    // 내부 공통
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
    private Stamp getStampOrThrow(Long stampId) {
        return stampRepository.findById(stampId)
                .orElseThrow(() -> new IllegalArgumentException("우표를 찾을 수 없습니다."));
    }

    // StampService 내부 아무 클래스/메서드 밖에 추가
    public static record UpgradeResultDto(
            Long fromStampId,
            Long toStampId,       // 업그레이드 안되면 null
            boolean upgraded,     // 수행 여부
            int costConsumed,     // 소모 개수(미수행이면 0)
            String message        // 사유 또는 "OK"
    ) {}

    @Transactional
    public List<UpgradeResultDto> upgradeAllEligible(Long userId) {
        var list = userStampRepository.findByUserIdWithStamp(userId);
        var results = new java.util.ArrayList<UpgradeResultDto>();

        for (var us0 : list) {
            // 잠금 걸고 최신 상태로 다시 로드
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

            int requiredLevel = requiredLevelFor(current.getRarity());
            if (us.getLevel() < requiredLevel) {
                results.add(new UpgradeResultDto(current.getId(), null, false, 0,
                        "레벨 부족 (필요: " + requiredLevel + ", 현재: " + us.getLevel() + ")"));
                continue;
            }

            var nextOpt = stampRepository.findBySiteUrlAndRarity(current.getSiteUrl(), next);
            if (nextOpt.isEmpty()) {
                results.add(new UpgradeResultDto(current.getId(), null, false, 0, "상위 희귀도 스탬프 없음"));
                continue;
            }

            int cost = costForUpgrade(current.getRarity());
            if (us.getCount() < cost) {
                results.add(new UpgradeResultDto(current.getId(), nextOpt.get().getId(), false, 0,
                        "수량 부족 (필요: " + cost + ", 현재: " + us.getCount() + ")"));
                continue;
            }

            // 수행: 원본에서 cost 차감, 상위 희귀도 1개 지급
            us.decreaseCount(cost);
            giveOne(nextOpt.get(), us.getUser());

            results.add(new UpgradeResultDto(current.getId(), nextOpt.get().getId(), true, cost, "OK"));
        }

        return results;
    }

    @Transactional
    public void rewardStampByMission(Long userId, String siteUrl, Rarity rarity) {
        User user = getUserOrThrow(userId);
        Stamp stamp = stampRepository.findBySiteUrlAndRarity(siteUrl, rarity)
                .orElseThrow(() -> new IllegalStateException("해당 사이트/희귀도의 우표가 등록되어 있지 않습니다."));
        giveOne(stamp, user);  // 이미 있으면 count++, 없으면 생성
    }

    @Transactional
    public boolean tryRewardStampByMission(Long userId, String siteUrl, Rarity rarity) {
        User user = getUserOrThrow(userId);
        return stampRepository.findBySiteUrlAndRarity(siteUrl, rarity)
                .map(stamp -> { giveOne(stamp, user); return true; })
                .orElse(false);
    }
}

