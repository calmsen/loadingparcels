package ru.calmsen.billing.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ParcelsBillingDto {
    private UUID messageId;
    private String user;
    private int trucksCount;
    private int parcelsCount;
    private int filledPlaces;
    private String operationType;
}
