package com.krafton.stamp.service;

import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.Mission;
import com.krafton.stamp.domain.User;
import com.krafton.stamp.domain.UserMission;
import com.krafton.stamp.repository.MissionRepository;
import com.krafton.stamp.repository.UserMissionRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MissionService {
    private final MissionRepository missionRepo;
    private final UserMissionRepository userMissionRepo;
    private final StampService stampService;

    // 방문 1회 기록
    @Transactional
    public boolean recordVisit(User user, String siteUrl) {
        Mission mission = missionRepo.findBySiteUrl(siteUrl)
                .orElseThrow(() -> new IllegalArgumentException("해당 사이트에 대한 미션이 존재하지 않습니다."));

        UserMission userMission = userMissionRepo.findByUserIdAndMissionId(user.getId(), mission.getId())
                .orElseGet(() -> {
                    UserMission newMission = UserMission.builder()
                            .user(user)
                            .mission(mission)
                            .currentVisits(0)
                            .completed(false)
                            .build();
                    return userMissionRepo.save(newMission);
                });

        if (userMission.isCompleted()) return true;

        userMission.increaseVisit();

        if (userMission.isCompleted()) {
            // 🔥 미션 완료 시 보상 지급
            stampService.rewardStampByMission(user.getId(), siteUrl, Rarity.COMMON); // Rarity는 예시
        }

        return userMission.isCompleted();
    }



}
