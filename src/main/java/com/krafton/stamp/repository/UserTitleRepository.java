package com.krafton.stamp.repository;

import com.krafton.stamp.domain.UserTitle;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface UserTitleRepository extends JpaRepository<UserTitle, Long> {
    List<UserTitle> findByUserId(Long userId);
    boolean existsByUserIdAndTitleId(Long userId, Long titleId);
    Optional<UserTitle> findByUserIdAndTitleId(Long userId, Long titleId);
    Optional<UserTitle> findFirstByUserIdAndRepresentativeTrue(Long userId);

    @Modifying
    @Query("update UserTitle ut set ut.representative = false where ut.user.id = :userId")
    void clearRepresentative(@Param("userId") Long userId);

    @Query("""
        select ut
        from UserTitle ut
        join fetch ut.title t
        where ut.user.id = :userId
    """)
    List<UserTitle> findByUserIdWithTitle(@Param("userId") Long userId);


}
