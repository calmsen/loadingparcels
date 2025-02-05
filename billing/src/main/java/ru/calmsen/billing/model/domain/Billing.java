package ru.calmsen.billing.model.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "billing")
@Getter
@Setter
@Builder
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

    public static final String DescriptionFormat = "%s;%s;%d машин;%d посылок;%.2f рублей";
}
