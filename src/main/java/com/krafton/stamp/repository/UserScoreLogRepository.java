package com.krafton.stamp.repository;

import com.krafton.stamp.domain.UserScoreLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserScoreLogRepository extends JpaRepository<UserScoreLog, Long> {
}
