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
public class SimpleLoadingAlgorithm implements LoadingAlgorithm {
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

        TruckLoaderHelper.checkMinTrucksCountBeforeLoad(parcels, trucks);

        Map<Truck, boolean[][]> filledPlaces = new HashMap<>();
        for (Truck truck : trucks) {
            filledPlaces.put(truck, new boolean[truck.getHeight()][truck.getWidth()]);
        }

        // отсортируем коробки по размерности от большего к меньшему
        parcels = parcels.stream().sorted(Comparator.comparingInt(Parcel::getDimensions).reversed()).collect(Collectors.toList());
        var parcelsQueue = new LinkedList<>(parcels);
        var currentParcel = parcelsQueue.poll();
        for (Truck currentTruck : trucks) {
            if (currentParcel == null) {
                break;
            }

            while (TruckLoaderHelper.canLoadParcel(currentParcel, currentTruck, filledPlaces.get(currentTruck))) {
                TruckLoaderHelper.loadParcel(currentParcel, currentTruck, filledPlaces.get(currentTruck));
                currentParcel = parcelsQueue.poll();
                if (currentParcel == null) {
                    break;
                }
            }
        }

        TruckLoaderHelper.checkMinTrucksCountAfterLoad(parcels, trucks);
    }
}
