package calmsen.service;


import calmsen.exception.ParcelValidatorException;
import calmsen.model.domain.Parcel;
import calmsen.model.domain.Truck;
import calmsen.model.domain.enums.LoadingMode;
import calmsen.model.domain.enums.ParcelDimensionsType;
import calmsen.model.domain.enums.ParcelJoinType;
import calmsen.util.ParcelsParser;
import calmsen.validator.ParcelValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class ParcelsService {
    public static final int TRUCK_WIDTH = 6;
    public static final int TRUCK_HEIGHT = 6;

    private final ParcelsParser parcelsParser;
    private final ParcelValidator parcelValidator;

    public List<Truck> loadParcels(String filePath, LoadingMode loadingMode) {
        var parcels = parcelsParser.parseParcelsFromFile(filePath);
        for (var parcel : parcels) {
            if (!parcelValidator.validate(parcel)) {
                throw new ParcelValidatorException("Не валидная посылка: \n" + parcel.toString());
            }
        }
        if (loadingMode == LoadingMode.SIMPLE){
            return simpleLoadParcels(parcels);
        }

        if (loadingMode == LoadingMode.EFFICIENT){
            return efficientLoadParcels(parcels);
        }

        throw new IllegalArgumentException("Режим " + loadingMode + "не поддерживается.");
    }

    private static List<Truck> simpleLoadParcels(List<Parcel> parcels) {
        return parcels.stream().map(x -> new Truck(TRUCK_WIDTH, TRUCK_HEIGHT, x)).collect(Collectors.toList());
    }

    private static List<Truck> efficientLoadParcels(List<Parcel> parcels) {
        parcels = packParcels(parcels);

        var trucks = new ArrayList<Truck>();
        loadTrucks(parcels, trucks);
        return trucks;
    }

    private static List<Parcel> packParcels(List<Parcel> parcels) {
        List<Parcel> newParcels = new ArrayList<>();

        log.info("Упакуем блоки до 6X3");
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X3From3X3, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        log.info("Упакуем блоки до 6X2");
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X2From3X2, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X2From7, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X2From4X2, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        log.info("Упакуем блоки до 6X1");
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X1From5X1, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X1From4X1, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        log.info("Упакуем нестандартные блоки");
        while (canJoinParcels(ParcelDimensionsType.Dimensions15From9, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions10From8, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions9From7, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        while (canJoinParcels(ParcelDimensionsType.Dimensions6X1From3X1, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        log.info("Упакуем блоки до 5X1");
        while (canJoinParcels(ParcelDimensionsType.Dimensions5X1, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        log.info("Упакуем блоки до 4X1");
        while (canJoinParcels(ParcelDimensionsType.Dimensions4X1, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        log.info("Упакуем блоки до 3X1");
        while (canJoinParcels(ParcelDimensionsType.Dimensions3X1, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        log.info("Упакуем блоки до 2X1");
        while (canJoinParcels(ParcelDimensionsType.Dimensions2X1, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }

        log.info("Упакуем блоки до 6X6");
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X6From6X3, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X6From6X4, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X6From6X3, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        while (canJoinParcels(ParcelDimensionsType.Dimensions6X6From6X4, parcels, newParcels)) {
            parcels = newParcels;
            newParcels = new ArrayList<Parcel>();
        }
        return parcels;
    }

    private static void loadTrucks(List<Parcel> parcels, ArrayList<Truck> trucks) {
        log.info("Загрузим блоки 6X6");
        var readyToLoadParcels = parcels.stream()
                .filter(x -> x.getDimensions() == 36)
                .toList();
        for (var parcel : readyToLoadParcels) {
            trucks.add(new Truck(TRUCK_WIDTH, TRUCK_HEIGHT, parcel));
        }

        log.info("Загрузим оставшееся блоки");
        var remainedParcels = parcels.stream()
                .filter(x -> x.getDimensions() < 36)
                .sorted(Comparator.comparingInt(x -> x.getWidth(0))).toList();

        var currentTruck = new Truck(TRUCK_WIDTH, TRUCK_HEIGHT, new ArrayList<>());
        for (var currentParcel : remainedParcels) {
            if (currentTruck.canLoadParcel(currentParcel)) {
                currentTruck.loadParcel(currentParcel);
            } else {
                trucks.add(currentTruck);
                currentTruck = new Truck(TRUCK_WIDTH, TRUCK_HEIGHT, new ArrayList<>());
            }
        }

        if (!currentTruck.isEmpty()){
            trucks.add(currentTruck);
        }
    }

    private static boolean canJoinParcels(
            ParcelDimensionsType targetDimensions,
            List<Parcel> parcels,
            List<Parcel> newParcels) {
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
            if (canPlaceByWidth(firstParcel, secondParcel, targetDimensions)){
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
            if (canJoinParcels(secondDimensionType, parcelsWithoutFirstParcel, newInnerParcels)) {
                var innerSecondParcel = newInnerParcels.getLast();
                if (canPlaceByWidth(firstParcel, innerSecondParcel, targetDimensions)){
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
            if (canJoinParcels(firstDimensionType, parcelsWithoutSecondParcel, newInnerParcels)) {
                var innerFirstParcel = newInnerParcels.getLast();
                if (canPlaceByWidth(innerFirstParcel, secondParcel, targetDimensions)){
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

    private static boolean canPlaceByWidth(Parcel firstParcel, Parcel secondParcel, ParcelDimensionsType targetDimensions) {
        return targetDimensions.getJoinType() == ParcelJoinType.HORIZONTAL
                && firstParcel.getWidth(0) + secondParcel.getWidth(0) > TRUCK_WIDTH;
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
