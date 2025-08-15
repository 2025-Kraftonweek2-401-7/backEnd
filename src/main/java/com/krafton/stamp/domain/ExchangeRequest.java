package com.krafton.stamp.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExchangeRequest {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private Stamp stampOffered;

    @ManyToOne
    private Stamp stampRequested;

    private String status; // PENDING, ACCEPTED, REJECTED
}
