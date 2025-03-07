package ru.calmsen.loadingparcels.service.loadingalgorithm;

import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

import java.util.Comparator;
import java.util.List;

/**
 * Максимально плотная погрузка.
 */
@Slf4j
public class EfficientLoadingAlgorithm extends LoadingAlgorithm {
    private final LoadingMode mode = LoadingMode.EFFICIENT;

    @Override
    public LoadingMode getMode() {
        return this.mode;
    }

    /**
     * Погружает посылки в машины.
     *
     * @param parcels список посылок
     * @param trucks  список машин
     */
    @Override
    public void loadParcels(List<Parcel> parcels, List<Truck> trucks) {
        if (parcels == null || parcels.isEmpty() || trucks == null || trucks.isEmpty()) {
            return;
        }

        checkMinTrucksCountBeforeLoad(parcels, trucks);

        var filledPlaces = createFilledPlaces(trucks);
        parcels = sortParcelsByDimension(parcels);
        for (Parcel parcel : parcels) {
            var parcelLoaded = false;

            for (Truck currentTruck : sortTrucksByFilledPlaces(trucks)) {
                if (canLoadParcel(parcel, currentTruck, filledPlaces.get(currentTruck))) {
                    loadParcel(parcel, currentTruck, filledPlaces.get(currentTruck));
                    parcelLoaded = true;
                    break;
                }
            }

            if (!parcelLoaded) {
                throw new BusinessException("Ни в какую машину не удалось погрузить посылку \n" + parcel);
            }
        }

        checkMinTrucksCountAfterLoad(parcels, trucks);
    }

    /**
     * Отсортируем машины по заполненности от большего к меньшему
     *
     * @param trucks список машин
     * @return список машин
     */
    private List<Truck> sortTrucksByFilledPlaces(List<Truck> trucks) {
        return trucks.stream().sorted(Comparator.comparingInt(Truck::getFilledPlaces).reversed()).toList();
    }
}
