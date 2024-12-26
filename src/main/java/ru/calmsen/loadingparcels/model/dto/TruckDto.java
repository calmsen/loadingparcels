package ru.calmsen.loadingparcels.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TruckDto {
    private int width;
    private int height;
    private List<PlacedBoxDto> boxes;
}
