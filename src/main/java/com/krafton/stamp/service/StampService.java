package com.krafton.stamp.service;

import com.krafton.stamp.domain.*;
import com.krafton.stamp.dto.StampCreateRequestDto;
import com.krafton.stamp.repository.StampRepository;
import com.krafton.stamp.repository.StampUpgradeRepository;
import com.krafton.stamp.repository.UserRepository;
import com.krafton.stamp.repository.UserStampRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StampService {

    private final UserRepository userRepository;
    private final StampRepository stampRepository;
    private final UserStampRepository userStampRepository;
    private final StampUpgradeRepository stampUpgradeRepository;
    /**
     * 우표 수집
     */
    public void collectStamp(Long userId, Long stampId) {
        User user = getUserOrThrow(userId);
        Stamp stamp = getStampOrThrow(stampId);
        giveStamp(user, stamp);
    }

    private void giveStamp(User user, Stamp stamp) {
        Optional<UserStamp> optional = userStampRepository.findByUserIdAndStampId(user.getId(), stamp.getId());

        if (optional.isPresent()) {
            optional.get().increaseCount();
        } else {
            UserStamp newStamp = UserStamp.builder()
                    .user(user)
                    .stamp(stamp)
                    .count(1)
                    .collectedAt(LocalDateTime.now())
                    .build();
            userStampRepository.save(newStamp);
        }
    }

    /**
     * 내가 모은 우표들 조회
     */
    @Transactional(readOnly = true)
    public List<UserStamp> getMyStamps(Long userId) {
        return userStampRepository.findByUserIdWithStamp(userId);
    }

    /**
     * 전체 우표 목록 (도감)
     */
    @Transactional(readOnly = true)
    public List<Stamp> getAllStamps() {
        return stampRepository.findAll();
    }

    /**
     * 특정 우표 상세 보기
     */
    @Transactional(readOnly = true)
    public Stamp getStampDetail(Long stampId) {
        return getStampOrThrow(stampId);
    }

    // 내부 공통 메서드
    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }

    private Stamp getStampOrThrow(Long stampId) {
        return stampRepository.findById(stampId)
                .orElseThrow(() -> new IllegalArgumentException("우표를 찾을 수 없습니다."));
    }

    /**
     * 미션 달성 보상: 특정 siteUrl + rarity 조건에 맞는 스탬프 지급
     */
    public void rewardStampByMission(Long userId, String siteUrl, Rarity rarity) {
        User user = getUserOrThrow(userId);
        Stamp stamp = stampRepository.findBySiteUrlAndRarity(siteUrl, rarity)
                .orElseThrow(() -> new IllegalStateException("해당 사이트와 희귀도에 맞는 우표가 없습니다."));
        giveStamp(user, stamp); // ✅ 중복 제거
    }


    @Transactional
    public void upgradeStamp(Long userId, Long fromStampId) {
        UserStamp userStamp = userStampRepository.findByUserIdAndStampId(userId, fromStampId)
                .orElseThrow(() -> new IllegalArgumentException("해당 우표를 보유하고 있지 않습니다."));

        // 업그레이드 가능한지 확인
        StampUpgrade upgrade = stampUpgradeRepository.findByFromStampId(fromStampId)
                .filter(u -> userStamp.getLevel() >= u.getRequiredLevel())
                .orElseThrow(() -> new IllegalArgumentException("업그레이드 조건을 충족하지 못했습니다."));

        // 기존 우표 삭제 or 카운트 감소
        userStampRepository.delete(userStamp); // 또는 userStamp.decreaseCount()

        // 업그레이드 우표 지급
        rewardStampByMission(userId, upgrade.getToStamp().getSiteUrl(), upgrade.getToStamp().getRarity());
    }

    @Transactional
    public void deleteStamp(Long stampId) {
        Stamp stamp = stampRepository.findById(stampId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 우표입니다."));
        stampRepository.delete(stamp);
    }

    //카테고리 별 도감 조회용 메소드
    @Transactional(readOnly = true)
    public List<Stamp> getStampsByCategory(Category category) {
        return stampRepository.findByCategory(category);
    }

    @Transactional
    public Stamp createStamp(StampCreateRequestDto req) {
        if (stampRepository.existsBySiteUrl(req.getSiteUrl())) {
            throw new IllegalArgumentException("이미 해당 사이트의 우표가 존재합니다.");
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



}

