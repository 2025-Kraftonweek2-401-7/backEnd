package com.krafton.stamp.repository;

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

}
