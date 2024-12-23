package ru.calmsen.loadingparcels.service.loadingalgorithm;

import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.model.domain.Box;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

import java.util.*;
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

    @Override
    public List<Truck> loadBoxes(List<Box> boxes, int truckWidth, int truckHeight, int trucksCount) {
        TruckLoaderHelper.checkMinTrucksCountBeforeLoad(boxes, truckWidth, truckHeight, trucksCount);

        List<Truck> trucks = new ArrayList<>();
        Map<Truck, boolean[][]> filledPlaces = new HashMap<>();
        for (int i = 0; i < trucksCount; i++) {
            trucks.add(new Truck(truckWidth, truckHeight));
            filledPlaces.put(trucks.get(i), new boolean[truckWidth][truckHeight]);
        }

        // отсортируем коробки по размерности
        boxes = boxes.stream().sorted(Comparator.comparingInt(Box::getDimensions).reversed()).collect(Collectors.toList());
        for (Box box : boxes) {
            // отсортируем машины по заполненности
            var sortedTrucks = trucks.stream().sorted(Comparator.comparingInt(x -> countFilledPlaces(x, filledPlaces))).toList();
            for (Truck currentTruck : sortedTrucks) {
                if (TruckLoaderHelper.canLoadBox(box, currentTruck, filledPlaces.get(currentTruck))) {
                    TruckLoaderHelper.loadBox(box, currentTruck, filledPlaces.get(currentTruck));
                    break;
                }
            }
        }

        TruckLoaderHelper.checkMinTrucksCountAfterLoad(trucksCount, trucks);
        return trucks;
    }


    public static int countFilledPlaces(Truck truck, Map<Truck, boolean[][]> filledPlaces){
        int count = 0;
        for (int i = 0; i < filledPlaces.get(truck).length; i++) {
            for (int j = 0; j < filledPlaces.get(truck)[i].length; j++) {
                count += filledPlaces.get(truck)[i][j] ? 1 : 0;
            }
        }
        return count;
    }
}
