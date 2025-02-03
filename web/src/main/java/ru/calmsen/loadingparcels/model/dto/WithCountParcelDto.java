package ru.calmsen.loadingparcels.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Dto модель посылки с подсчетом.
 */
@Getter
@Setter
@Builder
public class WithCountParcelDto {
    private ParcelDto parcel;
    private int quantity;
}
