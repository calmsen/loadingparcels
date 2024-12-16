package calmsen.model.domain;

import calmsen.model.domain.enums.ParcelDimensionsType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Parcel {
    /**
     * Тип посылки. От 1 до 9.
     */
    private int type;
    /**
     * Ширина посылки.
     */
    private int width;
    /**
     * Высота посылки.
     */
    private int height;

    /**
     * Верхняя ширина посылки.
     */
    private int topWidth;

    /**
     * Тип размеров.
     */
    private ParcelDimensionsType dimensionsType;
}
