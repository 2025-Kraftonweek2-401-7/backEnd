package com.krafton.stamp.config;

import com.krafton.stamp.domain.ConditionType;
import com.krafton.stamp.domain.Mission;
import com.krafton.stamp.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component

@RequiredArgsConstructor
public class MissionSeeder implements CommandLineRunner {

    private final MissionRepository missionRepo;

    @Override
    public void run(String... args) {
        addMissionIfNotExists("github.com", 5);
        addMissionIfNotExists("stackoverflow.com", 3);
        addMissionIfNotExists("spring.io", 2);
    }

    private void addMissionIfNotExists(String siteUrl, int targetValue) {
        missionRepo.findBySiteUrl(siteUrl)
                .orElseGet(() -> missionRepo.save(
                        Mission.builder()
                                .siteUrl(siteUrl)
                                .conditionType(ConditionType.VISIT_COUNT)
                                .targetValue(targetValue)
                                .build()
                ));
    }
}
