package com.krafton.stamp.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity @Table(name="user_title",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "title_id"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class UserTitle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="title_id", nullable=false)
    private Title title;

    private boolean representative;      // 대표 뱃지
    private LocalDateTime acquiredAt;
}
