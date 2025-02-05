package ru.calmsen.loadingparcels.model.dto;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
public class LoadParcelsBillingDto {
    public final UUID messageId;
    public final String user;
    public final int trucksCount;
    public final int parcelsCount;
    public final int filledPlaces;
}
