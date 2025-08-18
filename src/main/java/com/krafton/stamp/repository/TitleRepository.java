package com.krafton.stamp.repository;

import com.krafton.stamp.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TitleRepository extends JpaRepository<Title, Long> {

    @Query("""
        select t from Title t
        where t.active = true
          and (:category is null or t.category = :category)
          and (:rarity   is null or t.rarity   = :rarity)
    """)
    List<Title> findActiveByOptionalFilters(@Param("category") Category category,
                                            @Param("rarity") Rarity rarity);
}
