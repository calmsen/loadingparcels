package ru.calmsen.loadingparcels.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Dto модель размещенной посылки в машине.
 */
@Getter
@Setter
public class PlacedParcelDto {
    private ParcelDto parcel;
    private int positionX;
    private int positionY;
}
