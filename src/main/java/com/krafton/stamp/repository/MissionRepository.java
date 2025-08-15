package com.krafton.stamp.repository;

import com.krafton.stamp.domain.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MissionRepository extends JpaRepository<Mission, Long> {
    Optional<Mission> findBySiteUrl(String siteUrl);
}