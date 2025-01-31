package ru.calmsen.loadingparcels.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Доменная модель размещенной посылки в машине.
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class PlacedParcel {
    private final Parcel parcel;
    @Setter
    private int positionX;
    @Setter
    private int positionY;
}
