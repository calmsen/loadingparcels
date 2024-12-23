package ru.calmsen.loadingparcels.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlacedBoxDto {
    private BoxDto box;
    private int positionX;
    private int positionY;
}
