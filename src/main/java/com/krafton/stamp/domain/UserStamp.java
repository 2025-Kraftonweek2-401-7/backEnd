package com.krafton.stamp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
@Getter
@Entity
@Table(
        name = "user_stamp",
        uniqueConstraints = @UniqueConstraint(name = "uk_user_stamp", columnNames = {"user_id", "stamp_id"})
)
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
    public void decreaseCount(int amount) {
        if (amount <= 0) return;
        if (this.count < amount) {
            throw new IllegalStateException("업그레이드에 필요한 수량이 부족합니다. (필요: " + amount + ", 보유: " + this.count + ")");
        }
        this.count -= amount;
        this.level = calculateLevelFromCount(this.count); // 레벨 재계산
    }
}

