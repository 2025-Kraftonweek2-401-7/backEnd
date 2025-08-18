package com.krafton.stamp.repository;

import com.krafton.stamp.domain.Category;
import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.Stamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StampRepository extends JpaRepository<Stamp, Long> {
    Optional<Stamp> findByName(String name);
    Optional<Stamp> findBySiteUrlAndRarity(String siteUrl, Rarity rarity);
    List<Stamp> findByCategory(Category category);
    boolean existsBySiteUrlAndRarity(String siteUrl, Rarity rarity);

    // ✅ COMPLETE_SET용: 해당 카테고리/등급의 전체 스탬프 “종류 수”
    int countByCategoryAndRarity(Category category, Rarity rarity);
}
