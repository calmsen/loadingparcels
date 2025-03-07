package ru.calmsen.loadingparcels.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Размеры машины.
 * TODO: поменять сигнатуру метода loadParcels -> вместо списка Truck передать список TruckDimensions, а вместо void вернуть список Truck.
 */
@Getter
@RequiredArgsConstructor
public class TruckDimensions {
    private final int width;
    private final int height;
}
