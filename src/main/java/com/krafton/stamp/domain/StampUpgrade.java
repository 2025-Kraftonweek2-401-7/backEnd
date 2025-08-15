package com.krafton.stamp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StampUpgrade {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Stamp fromStamp;

    @ManyToOne
    private Stamp toStamp;

    private int requiredLevel;

    @Builder
    public StampUpgrade(Stamp fromStamp, Stamp toStamp, int requiredLevel) {
        this.fromStamp = fromStamp;
        this.toStamp = toStamp;
        this.requiredLevel = requiredLevel;
    }
}
