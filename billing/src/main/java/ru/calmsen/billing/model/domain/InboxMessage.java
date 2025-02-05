package ru.calmsen.billing.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "inbox")
public class InboxMessage {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String owner;
}

