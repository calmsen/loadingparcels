package ru.calmsen.loadingparcels.service.loadingalgorithm;

import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Простая погрузка посылок.
 */
@Slf4j
public class SimpleLoadingAlgorithm extends LoadingAlgorithm {
    private final LoadingMode mode = LoadingMode.SIMPLE;

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
        var parcelsQueue = new LinkedList<>(parcels);
        var currentParcel = parcelsQueue.poll();
        for (Truck currentTruck : trucks) {
            if (currentParcel == null) {
                break;
            }

            while (canLoadParcel(currentParcel, currentTruck, filledPlaces.get(currentTruck))) {
                loadParcel(currentParcel, currentTruck, filledPlaces.get(currentTruck));
                currentParcel = parcelsQueue.poll();
                if (currentParcel == null) {
                    break;
                }
            }
        }

        checkMinTrucksCountAfterLoad(parcels, trucks);
    }
}
