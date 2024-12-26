package ru.calmsen.loadingparcels.model.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class PlacedBox {
    private final Box box;
    @Setter
    private int positionX;
    @Setter
    private int positionY;
}
