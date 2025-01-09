package ru.calmsen.loadingparcels.service.loadingalgorithm;

import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

import java.util.List;

/**
 * Алгоритм погрузки посылок в машины.
 */
public interface LoadingAlgorithm {
    /**
     * Возвращает тип погрузки
     *
     * @return тип погрузки
     */
    LoadingMode getMode();

    /**
     * Погружает посылки в машины.
     *
     * @param parcels список посылок
     * @param trucks  список машин
     */
    void loadParcels(List<Parcel> parcels, List<Truck> trucks);
}
