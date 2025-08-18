package com.krafton.stamp.service;

import com.krafton.stamp.domain.*;
import com.krafton.stamp.repository.StampRepository;
import com.krafton.stamp.repository.UserRepository;
import com.krafton.stamp.repository.UserStampRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    private TitleService titleService;

    @Mock
    private ScoreService scoreService;

    private User user;
    private Stamp basicStamp, upgradedStamp;

    @BeforeEach
    void setUp() {
        user = User.builder().id(1L).build();

        basicStamp = Stamp.builder()
                .name("GitHub")
                .imageUrl("https://example.com/github.png")
                .siteUrl("github.com")
                .category(Category.BACKEND)
                .rarity(Rarity.COMMON)
                .description("desc")
                .build();
        setId(basicStamp,100L);

        upgradedStamp = Stamp.builder()
                .name("GitHub Gold")
                .imageUrl("https://example.com/github-gold.png")
                .siteUrl("github.com")
                .category(Category.BACKEND)
                .rarity(Rarity.RARE)
                .description("desc")
                .build();
        setId(upgradedStamp, 101L);
    }

    @Test
    void testCollectStamp_NewStamp() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(stampRepository.findById(100L)).thenReturn(Optional.of(basicStamp));
        when(userStampRepository.findByUserIdAndStampIdForUpdate(1L, 100L)).thenReturn(Optional.empty());

        stampService.collectStamp(1L, 100L);

        verify(userStampRepository).save(any(UserStamp.class));
        verify(scoreService).addScore(eq(1L), anyInt(), eq("COLLECT"), eq("STAMP"), eq(100L));
        verify(titleService).evaluateAndAward(1L, Category.BACKEND, Rarity.COMMON);
    }

    @Test
    void testCollectStamp_AlreadyHasStamp() {
        UserStamp userStamp = UserStamp.builder().user(user).stamp(basicStamp).count(1).collectedAt(LocalDateTime.now()).build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(stampRepository.findById(100L)).thenReturn(Optional.of(basicStamp));
        when(userStampRepository.findByUserIdAndStampIdForUpdate(1L, 100L)).thenReturn(Optional.of(userStamp));

        stampService.collectStamp(1L, 100L);

        assertEquals(2, userStamp.getCount());
        verify(scoreService).addScore(eq(1L), anyInt(), eq("COLLECT"), eq("STAMP"), eq(100L));
        verify(titleService).evaluateAndAward(1L, Category.BACKEND, Rarity.COMMON);
    }

    @Test
    void testUpgradeStamp_Success() {
        UserStamp userStamp = UserStamp.builder().user(user).stamp(basicStamp).count(15).collectedAt(LocalDateTime.now()).build();
        when(userStampRepository.findByUserIdAndStampIdForUpdate(1L, 100L)).thenReturn(Optional.of(userStamp));
        when(stampRepository.findBySiteUrlAndRarity("github.com", Rarity.RARE)).thenReturn(Optional.of(upgradedStamp));

        stampService.upgradeStampOrThrow(1L, 100L);

        assertEquals(5, userStamp.getCount());
        verify(scoreService).addScore(eq(1L), anyInt(), eq("UPGRADE"), eq("STAMP"), eq(101L));
        verify(titleService, times(2)).evaluateAndAward(eq(1L), any(), any());
        verify(userStampRepository).save(any(UserStamp.class));
    }
    private void setId(Object target, Long id) {
        try {
            Field idField = target.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(target, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
