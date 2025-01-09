package ru.calmsen.loadingparcels.service.loadingalgorithm;

import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Равномерная погрузка посылок.
 */
@Slf4j
public class UniformLoadingAlgorithm implements LoadingAlgorithm {
    private final LoadingMode mode = LoadingMode.UNIFORM;

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
        for (Parcel parcel : parcels) {
            var parcelLoaded = false;

            // отсортируем машины по заполненности от меньшего к большему
            for (Truck currentTruck : trucks.stream().sorted(Comparator.comparingInt(Truck::getFilledPlaces)).toList()) {
                if (TruckLoaderHelper.canLoadParcel(parcel, currentTruck, filledPlaces.get(currentTruck))) {
                    TruckLoaderHelper.loadParcel(parcel, currentTruck, filledPlaces.get(currentTruck));
                    parcelLoaded = true;
                    break;
                }
            }

            if (!parcelLoaded) {
                throw new BusinessException("Ни в какую машину не удалось погрузить посылку \n" + parcel);
            }
        }

        TruckLoaderHelper.checkMinTrucksCountAfterLoad(parcels, trucks);
    }
}
