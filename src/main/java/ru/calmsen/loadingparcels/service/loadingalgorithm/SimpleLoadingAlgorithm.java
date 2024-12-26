package ru.calmsen.loadingparcels.service.loadingalgorithm;

import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.model.domain.Box;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    @Override
    public List<Truck> loadBoxes(List<Box> boxes, int truckWidth, int truckHeight, int trucksCount) {
        if (boxes == null || boxes.isEmpty()) {
            return new ArrayList<>();
        }

        TruckLoaderHelper.checkMinTrucksCountBeforeLoad(boxes, truckWidth, truckHeight, trucksCount);

        // отсортируем коробки по размерности
        boxes = boxes.stream().sorted(Comparator.comparingInt(Box::getDimensions).reversed()).collect(Collectors.toList());
        var trucks = TruckLoaderHelper.loadBoxes(boxes, truckWidth, truckHeight);

        TruckLoaderHelper.checkMinTrucksCountAfterLoad(trucksCount, trucks);
        return trucks;
    }
}
