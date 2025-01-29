package ru.calmsen.loadingparcels.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "billing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "\"user\"", nullable = false)
    private String user;
    private String description;
    private String type;
    private LocalDate date;
    private int quantity;
    private BigDecimal cost;
}
