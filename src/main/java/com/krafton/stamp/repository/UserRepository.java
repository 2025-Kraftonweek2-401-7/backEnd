package com.krafton.stamp.repository;

import com.krafton.stamp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update User u set u.score = u.score + :delta where u.id = :userId")
    int incrementScore(@Param("userId") Long userId, @Param("delta") int delta);
}

