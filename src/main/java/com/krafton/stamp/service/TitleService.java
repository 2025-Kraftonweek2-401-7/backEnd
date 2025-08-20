package com.krafton.stamp.service;

import com.krafton.stamp.domain.*;
import com.krafton.stamp.domain.Title.ConditionType;
import com.krafton.stamp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TitleService {

    private final TitleRepository titleRepository;
    private final UserTitleRepository userTitleRepository;
    private final UserStampRepository userStampRepository;
    private final StampRepository stampRepository;

    @Transactional
    public List<Title> evaluateAndAward(Long userId, Category category, Rarity rarity) {
        // 바뀐 축(category/rarity)에 해당하는 칭호만 후보로
        List<Title> candidates = titleRepository.findActiveByOptionalFilters(category, rarity);
        List<Title> newly = new ArrayList<>();

        for (Title t : candidates) {
            if (userTitleRepository.existsByUserIdAndTitleId(userId, t.getId())) continue;
            if (meets(userId, t)) {
                UserTitle ut = UserTitle.builder()
                        .user(User.builder().id(userId).build()) // 프록시 참조로 충분
                        .title(t)
                        .acquiredAt(LocalDateTime.now())
                        .representative(false)
                        .build();
                userTitleRepository.save(ut);
                newly.add(t);
            }
        }
        return newly;
    }

    private boolean meets(Long userId, Title t) {
        Category c = t.getCategory();  // null이면 전체
        Rarity   r = t.getRarity();

        if (r == null) return false; // rarity는 필수

        return switch (t.getConditionType()) {
            case COMPLETE_SET -> {
                int owned = (c == null)
                        ? userStampRepository.countDistinctByUserAndRarity(userId, r)
                        : userStampRepository.countDistinctByUserAndCategoryAndRarity(userId, c, r);
                int total = (c == null)
                        ? stampRepository.countByRarity(r)
                        : stampRepository.countByCategoryAndRarity(c, r);
                yield total > 0 && owned >= total;
            }
            case DISTINCT_AT_LEAST -> {
                int ownedDistinct = (c == null)
                        ? userStampRepository.countDistinctByUserAndRarity(userId, r)
                        : userStampRepository.countDistinctByUserAndCategoryAndRarity(userId, c, r);
                yield ownedDistinct >= nz(t.getRequiredCount());
            }
            case TOTAL_AT_LEAST -> {
                int sum = (c == null)
                        ? userStampRepository.sumCountByUserAndRarity(userId, r)
                        : userStampRepository.sumCountByUserAndCategoryAndRarity(userId, c, r);
                yield sum >= nz(t.getRequiredCount());
            }
        };
    }


    private int nz(Integer i) { return i == null ? 0 : i; }

    // 대표 뱃지 설정
    @Transactional
    public void setRepresentative(Long userId, Long titleId) {
        if (!userTitleRepository.existsByUserIdAndTitleId(userId, titleId))
            throw new IllegalArgumentException("해당 칭호를 보유하고 있지 않습니다.");
        userTitleRepository.clearRepresentative(userId);
        var ut = userTitleRepository.findByUserIdAndTitleId(userId, titleId).orElseThrow();
        ut.setRepresentative(true);
    }

    @Transactional(readOnly = true)
    public List<UserTitle> getMyTitles(Long userId) {
        return userTitleRepository.findByUserIdWithTitle(userId); // ✅ fetch join 버전
    }

    @Transactional(readOnly = true)
    public Optional<UserTitle> getRepresentative(Long userId) {
        return userTitleRepository.findFirstByUserIdAndRepresentativeTrue(userId);
    }


}
