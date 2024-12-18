package calmsen.loadingalgorithm;

import calmsen.model.domain.Parcel;
import calmsen.model.domain.Truck;
import calmsen.model.domain.enums.LoadingMode;
import calmsen.model.domain.enums.ParcelDimensionsType;
import calmsen.model.domain.enums.ParcelJoinType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class EfficientLoadingAlgorithm extends LoadingAlgorithm {
    private final LoadingMode mode = LoadingMode.EFFICIENT;
    @Override
    public LoadingMode getMode() {
        return this.mode;
    }

    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, int truckWidth, int truckHeight) {
        parcels = packParcels(parcels, truckWidth);

        var trucks = new ArrayList<Truck>();
        loadTrucks(parcels, trucks, truckWidth, truckHeight);
        return trucks;
    }

    private static List<Parcel> packParcels(List<Parcel> parcels, int truckWidth) {
        List<Parcel> newParcels = new ArrayList<>();

        log.info("Упакуем блоки до 6X3");
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X3From3X3, parcels, newParcels, truckWidth)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        log.info("Упакуем блоки до 6X2");
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X2From3X2, parcels, newParcels, truckWidth)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X2From7, parcels, newParcels, truckWidth)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X2From4X2, parcels, newParcels, truckWidth)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        log.info("Упакуем блоки до 6X1");
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X1From5X1, parcels, newParcels, truckWidth)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X1From4X1, parcels, newParcels, truckWidth)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        while (canJoinParcels(ParcelDimensionsType.Dimensions6X1From3X1, parcels, newParcels, truckWidth)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        log.info("Упакуем блоки до 6X6");
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X6From6X3, parcels, newParcels, truckWidth)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X6From6X4, parcels, newParcels, truckWidth)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X6From6X3, parcels, newParcels, truckWidth)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X6From6X4, parcels, newParcels, truckWidth)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        return parcels;
    }

    private static void loadTrucks(List<Parcel> parcels, ArrayList<Truck> trucks, int truckWidth, int truckHeight) {
        log.info("Загрузим блоки 6X6");
        var readyToLoadParcels = parcels.stream()
                .filter(x -> x.getDimensions() == 36)
                .toList();
        for (var parcel : readyToLoadParcels) {
            trucks.add(new Truck(truckWidth, truckHeight, parcel));
        }

        log.info("Загрузим оставшееся блоки");
        // TODO: сделать загрузку оставшиеся блоков более эффективно ("как тетрис")
        var remainedParcels = parcels.stream()
                .filter(x -> x.getDimensions() < 36)
                .sorted(Comparator.comparingInt(Parcel::getDimensions)).toList();

        var currentTruck = new Truck(truckWidth, truckHeight, new ArrayList<>());
        for (var currentParcel : remainedParcels) {
            if (currentTruck.canLoadParcel(currentParcel)) {
                currentTruck.loadParcel(currentParcel);
            } else {
                trucks.add(currentTruck);
                currentTruck = new Truck(truckWidth, truckHeight, new ArrayList<>());
                currentTruck.loadParcel(currentParcel);
            }
        }

        if (!currentTruck.isEmpty()){
            trucks.add(currentTruck);
        }
    }

    private static boolean canJoinParcels(
            ParcelDimensionsType targetDimensions,
            List<Parcel> parcels,
            List<Parcel> newParcels,
            int truckWidth) {

        if (!newParcels.isEmpty()) {
            throw new IllegalArgumentException("Список newParcels должен быть пустой.");
        }

        var firstDimensionType = targetDimensions.getJoinPair().getFirstDimensionsForJoin();
        var secondDimensionType = targetDimensions.getJoinPair().getSecondDimensionsForJoin();

        if (firstDimensionType == null || secondDimensionType == null) {
            return false;
        }
        var firstParcel = parcels.stream()
                .filter(x -> x.getDimensions() == firstDimensionType.getDimensions() &&
                        (x.getDimensionsType() == null || x.getDimensionsType() == firstDimensionType))
                .findFirst().orElse(null);
        var secondParcel = parcels.stream()
                .filter(x -> x != firstParcel && x.getDimensions() == secondDimensionType.getDimensions() &&
                        (x.getDimensionsType() == null || x.getDimensionsType() == secondDimensionType))
                .findFirst().orElse(null);
        if (firstParcel != null && secondParcel != null) {
            if (canPlaceByWidth(firstParcel, secondParcel, targetDimensions, truckWidth)){
                return false;
            }
            var newParcel = joinParcels(firstParcel, secondParcel, targetDimensions.getJoinType());
            newParcels.addAll(parcels.stream().filter(x -> x != firstParcel && x != secondParcel).toList());
            newParcels.add(newParcel);
            return true;
        }

        if (firstParcel != null) {
            var parcelsWithoutFirstParcel = parcels.stream().filter(x -> x != firstParcel).toList();
            var newInnerParcels = new ArrayList<Parcel>();
            if (canJoinParcels(secondDimensionType, parcelsWithoutFirstParcel, newInnerParcels, truckWidth)) {
                var innerSecondParcel = newInnerParcels.getLast();
                if (canPlaceByWidth(firstParcel, innerSecondParcel, targetDimensions, truckWidth)){
                    return false;
                }
                var newParcel = joinParcels(firstParcel, innerSecondParcel, targetDimensions.getJoinType());
                newParcels.addAll(newInnerParcels.stream().filter(x -> x != firstParcel && x != innerSecondParcel).toList());
                newParcels.add(newParcel);
                return true;
            }
        }

        if (secondParcel != null) {
            var parcelsWithoutSecondParcel = parcels.stream().filter(x -> x != secondParcel).toList();
            var newInnerParcels = new ArrayList<Parcel>();
            if (canJoinParcels(firstDimensionType, parcelsWithoutSecondParcel, newInnerParcels, truckWidth)) {
                var innerFirstParcel = newInnerParcels.getLast();
                if (canPlaceByWidth(innerFirstParcel, secondParcel, targetDimensions, truckWidth)){
                    return false;
                }
                var newParcel = joinParcels(innerFirstParcel, secondParcel, targetDimensions.getJoinType());
                newParcels.addAll(newInnerParcels.stream().filter(x -> x != secondParcel && x != innerFirstParcel).toList());
                newParcels.add(newParcel);
                return true;
            }
        }

        return false;
    }

    private static boolean canPlaceByWidth(Parcel firstParcel, Parcel secondParcel, ParcelDimensionsType targetDimensions, int truckWidth) {
        return targetDimensions.getJoinType() == ParcelJoinType.HORIZONTAL
                && firstParcel.getWidth(0) + secondParcel.getWidth(0) > truckWidth;
    }

    private static Parcel joinParcels(Parcel firstParcel, Parcel secondParcel, ParcelJoinType mergeType) {
        if (mergeType == ParcelJoinType.HORIZONTAL) {
            if (firstParcel.getHeight() < secondParcel.getHeight()) {
                throw new IllegalArgumentException();
            }

            var skipSecondRows = firstParcel.getHeight() - secondParcel.getHeight();
            var content = new ArrayList<List<Character>>();
            for (int i = 0, j = 0; i < firstParcel.getHeight(); i++){
                var firstParcelRow = firstParcel.getContent().get(i);
                var secondParcelRow = i < skipSecondRows ? null : secondParcel.getContent().get(j++);
                if (secondParcelRow != null) {
                    content.add(Stream.concat(
                            firstParcelRow.stream(),
                            secondParcelRow.stream()
                    ).toList());
                }
                else {
                    content.add(firstParcelRow);
                }

            }

            return new Parcel(content, true);
        }

        var content = new ArrayList<>(firstParcel.getContent());
        content.addAll(secondParcel.getContent());
        return new Parcel(content);
    }
}
