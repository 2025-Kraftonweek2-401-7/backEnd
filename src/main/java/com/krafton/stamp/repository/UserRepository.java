package com.krafton.stamp.repository;

import com.krafton.stamp.domain.Stamp;
import com.krafton.stamp.domain.User;
import com.krafton.stamp.domain.UserStamp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}

