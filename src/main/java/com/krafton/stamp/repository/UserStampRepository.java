package com.krafton.stamp.repository;

import com.krafton.stamp.domain.UserStamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserStampRepository extends JpaRepository<UserStamp, Long> {
    List<UserStamp> findByUserId(Long userId);
    Optional<UserStamp> findByUserIdAndStampId(Long userId, Long stampId);

    @Query("SELECT us FROM UserStamp us JOIN FETCH us.stamp WHERE us.user.id = :userId")
    List<UserStamp> findByUserIdWithStamp(@Param("userId") Long userId);


}
