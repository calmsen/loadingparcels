package ru.calmsen.loadingparcels.service.loadingalgorithm;

import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Погрузка по одной посылке.
 */
@Slf4j
public class OneParcelLoadingAlgorithm extends LoadingAlgorithm {
    private final LoadingMode mode = LoadingMode.ONEPARCEL;

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
        for (Parcel parcel : parcels) {
            var parcelLoaded = false;

            var emptyTrucks = findEmptyTrucks(trucks);
            if (emptyTrucks.isEmpty()) {
                break;
            }

            for (Truck currentTruck : emptyTrucks) {
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

    private List<Truck> findEmptyTrucks(List<Truck> trucks) {
        return trucks.stream().filter(Truck::isEmpty).toList();
    }
}
