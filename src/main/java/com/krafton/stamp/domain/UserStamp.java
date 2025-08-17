package com.krafton.stamp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStamp {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "stamp_id")
    private Stamp stamp;

    private int count;  // 중복 수집 수
    private int level; //count -> level

    private LocalDateTime collectedAt;


    @Builder
    public UserStamp(User user, Stamp stamp, int count, LocalDateTime collectedAt) {
        this.user = user;
        this.stamp = stamp;
        this.count = count;
        this.collectedAt = collectedAt;
        this.level = calculateLevelFromCount(this.count);
    }
    public void increaseCount() {
        this.count++;
        this.level = calculateLevelFromCount(this.count);
    }
    private int calculateLevelFromCount(int count) {
        if (count >= 50) return 5;
        else if (count >= 30) return 4;
        else if (count >= 15) return 3;
        else if (count >= 5) return 2;
        else return 1;
    }
}

