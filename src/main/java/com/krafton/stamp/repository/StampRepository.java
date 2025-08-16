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
    boolean existsBySiteUrl(String siteUrl);

}
