package ru.calmsen.loadingparcels.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@Table(name = "outbox")
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "\"user\"", nullable = false)
    private String user;
}

