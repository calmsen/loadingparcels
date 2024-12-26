package ru.calmsen.loadingparcels.service.loadingalgorithm;

import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.Box;
import ru.calmsen.loadingparcels.model.domain.PlacedBox;
import ru.calmsen.loadingparcels.model.domain.Truck;

import java.util.ArrayList;
import java.util.List;

public class TruckLoaderHelper {
    public static List<Truck> loadBoxes(List<Box> boxes, int truckWidth, int truckHeight) {
        List<Truck> trucks = new ArrayList<>();
        var currentTruck = new Truck(truckWidth, truckHeight);
        var filledPlaces = new boolean[truckWidth][truckHeight];

        for (Box box : boxes) {
            if (!TruckLoaderHelper.canLoadBox(box, currentTruck, filledPlaces)) {
                trucks.add(currentTruck);
                currentTruck = new Truck(truckWidth, truckHeight);
                filledPlaces = new boolean[truckWidth][truckHeight];
            }
            TruckLoaderHelper.loadBox(box, currentTruck, filledPlaces);
        }

        trucks.add(currentTruck);
        return trucks;
    }

    public static boolean canLoadBox(Box box, Truck truck, boolean[][] filledPlaces) {
        for (int currentY = 0; currentY <= truck.getHeight() - box.getHeight(); currentY++) {
            for (int currentX = 0; currentX <= truck.getWidth() - box.getWidth(); currentX++) {
                if (isFreePlace(box, filledPlaces, currentY, currentX)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isFreePlace(Box box, boolean[][] filledPlaces, int y, int x) {
        for (int i = box.getHeight() - 1, currentY = y; i >= 0; i--, y++) {
            for (int j = 0, currentX = x; j < box.getWidth(i); j++, currentX++) {
                if (filledPlaces[currentY][currentX]) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void loadBox(Box box, Truck truck, boolean[][] filledPlaces) {
        for (int currentY = 0; currentY <= truck.getHeight() - box.getHeight(); currentY++) {
            for (int currentX = 0; currentX <= truck.getWidth() - box.getWidth(); currentX++) {
                if (isFreePlace(box, filledPlaces, currentY, currentX)) {
                    loadBox(box, truck, filledPlaces, currentY, currentX);
                    return;
                }
            }
        }
    }

    private static void loadBox(Box box, Truck truck, boolean[][] filledPlaces, int y, int x) {
        for (int i = box.getHeight() - 1, currentY = y; i >= 0; i--, currentY++) {
            for (int j = 0, currentX = x; j < box.getWidth(i); j++, currentX++) {
                filledPlaces[currentY][currentX] = true;
            }
        }

        truck.loadBox(new PlacedBox(box, x, y));
    }

    public static void checkMinTrucksCountBeforeLoad(List<Box> boxes, int truckWidth, int truckHeight, int trucksCount) {
        var minDimensions = boxes.stream().mapToInt(Box::getDimensions).sum();
        if (trucksCount * truckWidth * truckHeight < minDimensions) {
            throw new BusinessException("Не достаточно машин для погрузки. Минимальное количество: " + (int) Math.ceil(minDimensions / truckWidth / truckHeight));
        }
    }

    public static void checkMinTrucksCountAfterLoad(int trucksCount, List<Truck> trucks) {
        if (trucksCount < trucks.size()) {
            throw new BusinessException("Не достаточно машин для погрузки. Необходимое количество: " + trucks.size());
        }
    }
}
