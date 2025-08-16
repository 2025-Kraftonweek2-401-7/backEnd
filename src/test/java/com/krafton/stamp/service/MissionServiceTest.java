package com.krafton.stamp.service;

import com.krafton.stamp.domain.*;
import com.krafton.stamp.repository.MissionRepository;
import com.krafton.stamp.repository.UserMissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MissionServiceTest {

    @InjectMocks
    private MissionService missionService;

    @Mock
    private MissionRepository missionRepo;

    @Mock
    private UserMissionRepository userMissionRepo;

    @Mock
    private StampService stampService;

    private final String siteUrl = "github.com";
    private User testUser;
    private Mission testMission;

    @BeforeEach
    void setup() {
        testUser = User.builder()
                .username("testuser")
                .email("email@test.com")
                .password("pass")
                .score(0)
                .build();

        testMission = Mission.builder()
                .siteUrl(siteUrl)
                .targetValue(3)
                .conditionType(ConditionType.VISIT_COUNT)
                .build();
    }


    //User가 해당 사이트 처음 방문했을 때 UserMission이 생성되고 방문 수가 1증가하는지
    @Test
    void testFirstVisit_CreatesUserMissionAndRecordsVisit() {
        // given
        when(missionRepo.findBySiteUrl(siteUrl)).thenReturn(Optional.of(testMission));
        when(userMissionRepo.findByUserIdAndMissionId(any(), any())).thenReturn(Optional.empty());

        UserMission dummyUM = UserMission.builder()
                .user(testUser)
                .mission(testMission)
                .currentVisits(1)
                .completed(false)
                .build();

        when(userMissionRepo.save(any(UserMission.class))).thenReturn(dummyUM);

        // when
        boolean result = missionService.recordVisit(testUser, siteUrl);

        // then
        assertThat(result).isFalse(); // 아직 completed 아님
        verify(userMissionRepo).save(any(UserMission.class));
    }


    //유저가 이미 UserMission을 가지고 있는 경우 방문 횟수만 증가
    @Test
    void testVisitCompletesMission() {
        // given
        UserMission um = UserMission.builder()
                .user(testUser)
                .mission(testMission)
                .currentVisits(2) // 목표: 3, 이번 방문으로 달성
                .completed(false)
                .build();

        when(missionRepo.findBySiteUrl(siteUrl)).thenReturn(Optional.of(testMission));
        when(userMissionRepo.findByUserIdAndMissionId(any(), any())).thenReturn(Optional.of(um));

        // when
        boolean result = missionService.recordVisit(testUser, siteUrl);

        // then
        assertThat(result).isTrue(); // 달성 완료
        assertThat(um.getCurrentVisits()).isEqualTo(3);
        assertThat(um.isCompleted()).isTrue();
    }


    //User의 방문 횟수가 목표치에 도달해서 미션이 완료됨
    @Test
    void testAlreadyCompletedMission_NoEffect() {
        // given
        UserMission um = UserMission.builder()
                .user(testUser)
                .mission(testMission)
                .currentVisits(3)
                .completed(true)
                .build();

        when(missionRepo.findBySiteUrl(siteUrl)).thenReturn(Optional.of(testMission));
        when(userMissionRepo.findByUserIdAndMissionId(any(), any())).thenReturn(Optional.of(um));

        // when
        boolean result = missionService.recordVisit(testUser, siteUrl);

        // then
        assertThat(result).isTrue(); // 이미 완료된 미션
        assertThat(um.getCurrentVisits()).isEqualTo(3); // 방문 증가 없음
        verify(userMissionRepo, never()).save(any());
    }

    @Test
    void testVisitCompletesMission_AndRewardsStamp() {
        // given
        when(missionRepo.findBySiteUrl(siteUrl)).thenReturn(Optional.of(testMission));
        when(userMissionRepo.findByUserIdAndMissionId(any(), any())).thenReturn(Optional.empty());

        // ArgumentCaptor로 저장 요청 객체 추적
        ArgumentCaptor<UserMission> captor = ArgumentCaptor.forClass(UserMission.class);

        // save는 받은 객체 그대로 반환
        when(userMissionRepo.save(any(UserMission.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when: 실제로 recordVisit 호출 (내부에서 increaseVisit 작동해야 함)
        boolean result = missionService.recordVisit(testUser, siteUrl);

        // then

        verify(userMissionRepo).save(captor.capture());
        UserMission savedMission = captor.getValue();

        assertThat(result).isFalse(); // 이번 visit 하나만으로는 아직 완료 안 됨 (목표 3회니까)
        assertThat(savedMission.getCurrentVisits()).isEqualTo(1);
        assertThat(savedMission.isCompleted()).isFalse();

        // ✅ 보상 안 줘야 정상
        verify(stampService, never()).rewardStampByMission(any(), any(), any());
    }


}
