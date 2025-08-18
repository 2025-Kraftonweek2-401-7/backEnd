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
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.test.context.ActiveProfiles;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
                .id(userId)
                .username("testuser")
                .email("test@email.com")
                .password("password")
                .score(0)
                .build();

        basicStamp = Stamp.builder()
                .name("GitHub")
                .imageUrl("https://example.com/github.png")
                .siteUrl("github.com")
                .category(Category.BACKEND)
                .rarity(Rarity.COMMON)
                .description("desc")
                .build();
        setId(basicStamp, stampId);

        upgradedStamp = Stamp.builder()
                .name("GitHub Gold")
                .imageUrl("https://example.com/github-gold.png")
                .siteUrl("github.com")
                .category(Category.BACKEND)
                .rarity(Rarity.RARE)
                .description("desc")
                .build();
        setId(upgradedStamp, upgradedStampId);
    }

    static void setId(Object entity, Long id) {
        try {
            Field field = entity.getClass().getDeclaredField("id");
            field.setAccessible(true);
            field.set(entity, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCollectStamp_NewStamp() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(stampRepository.findById(stampId)).thenReturn(Optional.of(basicStamp));
        when(userStampRepository.findByUserIdAndStampId(userId, stampId)).thenReturn(Optional.empty());

        stampService.collectStamp(userId, stampId);

        verify(userStampRepository).save(any(UserStamp.class));
    }

    @Test
    void testCollectStamp_AlreadyHasStamp() {
        UserStamp owned = UserStamp.builder()
                .user(testUser)
                .stamp(basicStamp)
                .count(1)
                .collectedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(stampRepository.findById(stampId)).thenReturn(Optional.of(basicStamp));
        when(userStampRepository.findByUserIdAndStampIdForUpdate(userId, stampId))
                .thenReturn(Optional.of(owned));

        stampService.collectStamp(userId, stampId);

        assertEquals(2, owned.getCount()); // count 증가 확인
        verify(userStampRepository, never()).save(any()); // save 호출 안 됨
    }

    @Test
    void testUpgradeStamp_Success() {
        UserStamp owned = UserStamp.builder()
                .user(testUser)
                .stamp(basicStamp)
                .count(50)
                .collectedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userStampRepository.findByUserIdAndStampIdForUpdate(userId, stampId))
                .thenReturn(Optional.of(owned));
        when(stampRepository.findBySiteUrlAndRarity("github.com", Rarity.RARE))
                .thenReturn(Optional.of(upgradedStamp));

        stampService.upgradeStampOrThrow(userId, stampId);

        verify(userStampRepository).save(any(UserStamp.class));
    }

    @Test
    void testUpgradeStamp_Fail_TooLowLevel() {
        UserStamp owned = UserStamp.builder()
                .user(testUser)
                .stamp(basicStamp)
                .count(1)
                .collectedAt(LocalDateTime.now())
                .build();

        simulateCount(owned, 30); // level 4

        StampUpgrade upgrade = StampUpgrade.builder()
                .fromStamp(basicStamp)
                .toStamp(upgradedStamp)
                .requiredLevel(5)
                .build();

        when(userStampRepository.findByUserIdAndStampId(userId, stampId)).thenReturn(Optional.of(owned));
        when(stampUpgradeRepository.findByFromStampId(stampId)).thenReturn(Optional.of(upgrade));

        assertThrows(IllegalArgumentException.class, () ->
                stampService.upgradeStampOrThrow(userId, stampId));
    }

    @Test
    void testDeleteStamp() {
        when(stampRepository.findById(stampId)).thenReturn(Optional.of(basicStamp));

        stampService.deleteStamp(stampId);

        verify(stampRepository).delete(basicStamp);
    }

    private void simulateCount(UserStamp userStamp, int count) {
        for (int i = 0; i < count; i++) {
            userStamp.increaseCount();
        }
    }
}
