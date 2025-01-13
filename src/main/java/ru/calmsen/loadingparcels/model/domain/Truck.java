package ru.calmsen.loadingparcels.model.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Доменная модель машины.
 */
@Getter
public class Truck {
    private final int width;
    private final int height;
    private final List<PlacedParcel> parcels;

    public Truck(int width, int height) {
        this.width = width;
        this.height = height;
        this.parcels = new ArrayList<>();
    }

    public Truck(int width, int height, PlacedParcel parcel) {
        this.width = width;
        this.height = height;
        this.parcels = List.of(parcel);
    }

    /**
     * Загружает посылку в машину
     * @param parcel информация о посылке с координатами.
     */
    public void loadParcel(PlacedParcel parcel) {
        this.parcels.add(parcel);
    }

    /**
     * Получается количество заполненные мест в машине.
     * @return количество заполненные мест
     */
    public int getFilledPlaces() {
        return this.parcels.stream().mapToInt(x -> x.getParcel().getDimensions()).sum();
    }

    /**
     * Проверяет пустая ли машина
     * @return true если машина пустая. Иначе false
     */
    public boolean isEmpty() {
        return this.parcels.isEmpty();
    }
}
