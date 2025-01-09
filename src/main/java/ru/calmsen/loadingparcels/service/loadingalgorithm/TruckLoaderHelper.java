package ru.calmsen.loadingparcels.service.loadingalgorithm;

import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.PlacedParcel;
import ru.calmsen.loadingparcels.model.domain.Truck;

import java.util.List;

public class TruckLoaderHelper {
    /**
     * Проверяет можно ли загрузить посылку.
     *
     * @param parcel       объект посылки
     * @param truck        объект машины
     * @param filledPlaces заполненные места
     * @return true если можно, иначе false
     */
    public static boolean canLoadParcel(Parcel parcel, Truck truck, boolean[][] filledPlaces) {
        for (int currentY = 0; currentY <= truck.getHeight() - parcel.getHeight(); currentY++) {
            for (int currentX = 0; currentX <= truck.getWidth() - parcel.getWidth(); currentX++) {
                if (isFreePlace(parcel, filledPlaces, currentY, currentX)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Проверяет свободно ли место для размещения посылки по указанной позиции.
     *
     * @param parcel       объект посылки
     * @param filledPlaces заполненные места
     * @param y            координата
     * @param x            координата
     * @return true если можно, иначе false
     */
    private static boolean isFreePlace(Parcel parcel, boolean[][] filledPlaces, int y, int x) {
        for (int i = parcel.getHeight() - 1, currentY = y; i >= 0; i--, currentY++) {
            for (int j = 0, currentX = x; j < parcel.getWidth(i); j++, currentX++) {
                if (parcel.getContent().get(i).get(j) == ' ') {
                    continue;
                }
                try {
                    if (filledPlaces[currentY][currentX]) {
                        return false;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw e;
                }
            }
        }

        return true;
    }

    /**
     * Загружает посылку в машину.
     *
     * @param parcel       объект посылки
     * @param truck        объект машины
     * @param filledPlaces заполненные места
     */
    public static void loadParcel(Parcel parcel, Truck truck, boolean[][] filledPlaces) {
        for (int currentY = 0; currentY <= truck.getHeight() - parcel.getHeight(); currentY++) {
            for (int currentX = 0; currentX <= truck.getWidth() - parcel.getWidth(); currentX++) {
                if (isFreePlace(parcel, filledPlaces, currentY, currentX)) {
                    loadParcel(parcel, truck, filledPlaces, currentY, currentX);
                    return;
                }
            }
        }
    }

    /**
     * Загружает посылку в машину в указанную позицию.
     *
     * @param parcel       объект посылки
     * @param filledPlaces заполненные места
     * @param y            координата
     * @param x            координата
     */
    private static void loadParcel(Parcel parcel, Truck truck, boolean[][] filledPlaces, int y, int x) {
        for (int i = parcel.getHeight() - 1, currentY = y; i >= 0; i--, currentY++) {
            for (int j = 0, currentX = x; j < parcel.getWidth(i); j++, currentX++) {
                if (parcel.getContent().get(i).get(j) == ' ') {
                    continue;
                }
                filledPlaces[currentY][currentX] = true;
            }
        }

        truck.loadParcel(new PlacedParcel(parcel, x, y));
    }

    /**
     * Проверяет достаточно ли машин для погрузки перед началом погрузки.
     *
     * @param parcels список посылок
     * @param trucks  список машин
     */
    public static void checkMinTrucksCountBeforeLoad(List<Parcel> parcels, List<Truck> trucks) {
        var parcelsDimensions = parcels.stream().mapToInt(Parcel::getDimensions).sum();
        var trucksDimensions = trucks.stream().mapToInt(x -> x.getWidth() * x.getHeight()).sum();
        if (trucksDimensions < parcelsDimensions) {
            throw new BusinessException("Не достаточно машин для погрузки");
        }
    }

    /**
     * Проверяет достаточно ли машин для погрузки после погрузки.
     *
     * @param parcels список посылок
     * @param trucks  список машин
     */
    public static void checkMinTrucksCountAfterLoad(List<Parcel> parcels, List<Truck> trucks) {
        var parcelsDimensions = parcels.stream().mapToInt(Parcel::getDimensions).sum();
        var trucksFilledPlaces = trucks.stream().mapToInt(Truck::getFilledPlaces).sum();
        if (trucksFilledPlaces < parcelsDimensions) {
            throw new BusinessException("Не достаточно машин для погрузки");
        }
    }
}
