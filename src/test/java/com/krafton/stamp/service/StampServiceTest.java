package com.krafton.stamp.service;


import com.krafton.stamp.domain.*;
import com.krafton.stamp.repository.StampRepository;
import com.krafton.stamp.repository.StampUpgradeRepository;
import com.krafton.stamp.repository.UserRepository;
import com.krafton.stamp.repository.UserStampRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StampServiceTest {

    @InjectMocks
    private StampService stampService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StampRepository stampRepository;

    @Mock
    private UserStampRepository userStampRepository;

    @Mock
    private StampUpgradeRepository stampUpgradeRepository;

    private final Long userId = 1L;
    private final Long stampId = 100L;
    private final Long upgradedStampId = 200L;

    private User testUser;
    private Stamp basicStamp;
    private Stamp upgradedStamp;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .username("testuser")
                .email("test@email.com")
                .password("password")
                .score(0)
                .build();

        basicStamp = Stamp.builder()
                .name("GitHub")
                .imageUrl("url")
                .rarity(Rarity.COMMON)
                .description("desc")
                .build();

        upgradedStamp = Stamp.builder()
                .name("GitHub Gold")
                .imageUrl("url")
                .rarity(Rarity.RARE)
                .description("desc")
                .build();
    }


    @Test
    void testCollectStamp_NewStamp() {
        // given
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(stampRepository.findById(stampId)).thenReturn(Optional.of(basicStamp));
        when(userStampRepository.findByUserIdAndStampId(userId, stampId)).thenReturn(Optional.empty());

        // when
        stampService.collectStamp(userId, stampId);

        // then
        verify(userStampRepository).save(any(UserStamp.class));
    }

    @Test
    void testCollectStamp_AlreadyHasStamp() {
        // given
        UserStamp owned = UserStamp.builder()
                .user(testUser)
                .stamp(basicStamp)
                .count(1)
                .collectedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(stampRepository.findById(stampId)).thenReturn(Optional.of(basicStamp));
        when(userStampRepository.findByUserIdAndStampId(userId, stampId)).thenReturn(Optional.of(owned));

        // when
        stampService.collectStamp(userId, stampId);

        // then
        verify(userStampRepository, never()).save(any());
        assertEquals(2, owned.getCount()); // increaseCount() 확인
    }

    @Test
    void testUpgradeStamp_Success() {
        // given
        UserStamp owned = UserStamp.builder()
                .user(testUser)
                .stamp(basicStamp)
                .count(1)
                .collectedAt(LocalDateTime.now())
                .build();

        simulateCount(owned, 49); // count = 50 → level = 5

        StampUpgrade upgrade = StampUpgrade.builder()
                .fromStamp(basicStamp)
                .toStamp(upgradedStamp)
                .requiredLevel(3)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser)); // 🔥 추가
        when(userStampRepository.findByUserIdAndStampId(userId, stampId))
                .thenReturn(Optional.of(owned));
        when(stampUpgradeRepository.findByFromStampId(stampId))
                .thenReturn(Optional.of(upgrade));
        when(stampRepository.findBySiteUrlAndRarity(upgradedStamp.getSiteUrl(), upgradedStamp.getRarity()))
                .thenReturn(Optional.of(upgradedStamp));

        // when
        stampService.upgradeStamp(userId, stampId);

        // then
        verify(userStampRepository).delete(owned);
        verify(userStampRepository).save(any(UserStamp.class)); // rewardStampByMission
    }


    @Test
    void testUpgradeStamp_Fail_TooLowLevel() {
        // given
        UserStamp owned = UserStamp.builder()
                .user(testUser)
                .stamp(basicStamp)
                .count(0)
                .collectedAt(LocalDateTime.now())
                .build();

        simulateCount(owned, 30); // count = 30, level = 4

        StampUpgrade upgrade = StampUpgrade.builder()
                .fromStamp(basicStamp)
                .toStamp(upgradedStamp)
                .requiredLevel(3)
                .build();

        when(userStampRepository.findByUserIdAndStampId(userId, stampId))
                .thenReturn(Optional.of(owned));
        when(stampUpgradeRepository.findByFromStampId(stampId))
                .thenReturn(Optional.of(upgrade));

        // when & then
        assertThrows(IllegalArgumentException.class, () ->
                stampService.upgradeStamp(userId, stampId));
    }

    @Test
    void testDeleteStamp() {
        Long stampId = 1L;

        Stamp stamp = Stamp.builder()
                .name("Test Stamp")
                .description("테스트 용 우표")
                .rarity(Rarity.COMMON)
                .build();

        when(stampRepository.findById(stampId)).thenReturn(Optional.of(stamp));

        stampService.deleteStamp(stampId);

        verify(stampRepository).delete(stamp);
    }


    static void simulateCount(UserStamp userStamp, int count) {
        for (int i = 0; i < count; i++) {
            userStamp.increaseCount();
        }
    }

}
