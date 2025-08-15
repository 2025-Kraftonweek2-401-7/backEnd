package com.krafton.stamp.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_mission",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_mission", columnNames = {"user_id", "mission_id"})
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 진행 주체
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 어떤 미션인지
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    // 현재 방문 횟수(초기 0)
    @Builder.Default
    @Column(name = "current_visits", nullable = false)
    private Integer currentVisits = 0;

    // 완료 여부
    @Builder.Default
    @Column(name = "completed", nullable = false)
    private boolean completed = false;

    // 마지막 갱신 시각(자동 업데이트)
    @UpdateTimestamp
    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    // ==== 도메인 메서드 ====

    /** 방문 1회 누적 */
    public void increaseVisit() {
        this.currentVisits = this.currentVisits + 1;

        // ✅ 조건 충족 시 자동 완료 처리
        if (!this.completed && this.currentVisits >= this.mission.getTargetValue()) {
            this.completed = true;
        }
    }

    /** 완료 처리 */
    public void markCompleted() {
        this.completed = true;
    }

    /** 완료 해제(필요 시) */
    public void resetCompleted() {
        this.completed = false;
    }
}
