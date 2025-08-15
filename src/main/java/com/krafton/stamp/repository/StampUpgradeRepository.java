package com.krafton.stamp.repository;

import com.krafton.stamp.domain.StampUpgrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StampUpgradeRepository extends JpaRepository<StampUpgrade, Long> {
    Optional<StampUpgrade> findByFromStampId(Long fromStampId);
}
