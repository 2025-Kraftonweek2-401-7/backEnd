package com.krafton.stamp.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_score_log")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserScoreLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="user_id", nullable=false)
    private User user;

    private int delta;              // +10, +25 ...
    private String reason;          // COLLECT / UPGRADE / TITLE
    private String refType;         // STAMP / TITLE
    private Long refId;             // stampId or titleId
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
