package com.krafton.stamp.service;

import com.krafton.stamp.domain.Rarity;
import com.krafton.stamp.domain.User;
import com.krafton.stamp.domain.UserScoreLog;
import com.krafton.stamp.repository.UserRepository;
import com.krafton.stamp.repository.UserScoreLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScoreService {

    private final UserRepository userRepository;
    private final UserScoreLogRepository logRepository; // 이제 존재

    @Transactional
    public void addScore(Long userId, int delta, String reason, String refType, Long refId) {
        if (delta == 0) return;
        userRepository.incrementScore(userId, delta);

        logRepository.save(
                UserScoreLog.builder()
                        .user(User.builder().id(userId).build())
                        .delta(delta)
                        .reason(reason)
                        .refType(refType)
                        .refId(refId)
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    public int pointsForCollect(Rarity r) {
        return switch (r) {
            case COMMON    -> 1;
            case RARE      -> 3;
            case EPIC      -> 10;
            case LEGENDARY -> 25;
        };
    }

    public int bonusForUpgrade(Rarity to) {
        return switch (to) {
            case COMMON    -> 0;
            case RARE      -> 5;
            case EPIC      -> 15;
            case LEGENDARY -> 40;
        };
    }
}
