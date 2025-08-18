package com.krafton.stamp.repository;

import com.krafton.stamp.domain.Category;
import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.UserStamp;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserStampRepository extends JpaRepository<UserStamp, Long> {

    List<UserStamp> findByUserId(Long userId);
    Optional<UserStamp> findByUserIdAndStampId(Long userId, Long stampId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select us from UserStamp us where us.user.id = :userId and us.stamp.id = :stampId")
    Optional<UserStamp> findByUserIdAndStampIdForUpdate(@Param("userId") Long userId, @Param("stampId") Long stampId);

    @Query("select us from UserStamp us join fetch us.stamp where us.user.id = :userId")
    List<UserStamp> findByUserIdWithStamp(@Param("userId") Long userId);

    @Query("""
        select us from UserStamp us
        join fetch us.stamp s
        where us.user.id = :userId and s.rarity = :rarity
    """)
    List<UserStamp> findByUserIdAndRarity(@Param("userId") Long userId, @Param("rarity") Rarity rarity);

    @Query("""
        select us
        from UserStamp us
        join fetch us.stamp s
        where us.user.id = :userId
          and s.category = :category
        order by s.rarity desc, s.name asc
    """)
    List<UserStamp> findByUserIdAndCategory(@Param("userId") Long userId, @Param("category") Category category);

    @Query("""
        select coalesce(sum(us.count), 0)
        from UserStamp us
        join us.stamp s
        where us.user.id = :userId
          and s.category = :category
    """)
    int sumCountByUserAndCategory(@Param("userId") Long userId, @Param("category") Category category);

    // ✅ “소유한 서로 다른 스탬프 종류 수” (distinct)
    @Query("""
        select count(distinct s.id)
        from UserStamp us
        join us.stamp s
        where us.user.id = :userId
          and s.category = :category
          and s.rarity   = :rarity
    """)
    int countDistinctByUserAndCategoryAndRarity(@Param("userId") Long userId,
                                                @Param("category") Category category,
                                                @Param("rarity") Rarity rarity);

    // ✅ “총 보유 개수 합”
    @Query("""
        select coalesce(sum(us.count), 0)
        from UserStamp us
        join us.stamp s
        where us.user.id = :userId
          and s.category = :category
          and s.rarity   = :rarity
    """)
    int sumCountByUserAndCategoryAndRarity(@Param("userId") Long userId,
                                           @Param("category") Category category,
                                           @Param("rarity") Rarity rarity);
}

