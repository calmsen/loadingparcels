package ru.calmsen.loadingparcels.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "outbox")
public class OutboxMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String messageType;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String owner;
}

