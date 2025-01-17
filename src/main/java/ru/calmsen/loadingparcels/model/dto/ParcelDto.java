package ru.calmsen.loadingparcels.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Dto модель посылки.
 */
@Getter
@Setter
public class ParcelDto {
    private int width;
    private int height;
    private String form;
    private int dimensions;
    private char symbol;
    private String name;
}
