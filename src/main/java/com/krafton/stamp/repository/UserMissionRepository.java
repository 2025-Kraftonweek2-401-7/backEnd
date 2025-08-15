package com.krafton.stamp.repository;

import com.krafton.stamp.domain.UserMission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    Optional<UserMission> findByUserIdAndMissionId(Long userId, Long missionId);
}