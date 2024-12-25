package ru.calmsen.loadingparcels.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.exception.ParcelValidatorException;
import ru.calmsen.loadingparcels.model.domain.Box;
import ru.calmsen.loadingparcels.model.domain.PlacedBox;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;
import ru.calmsen.loadingparcels.service.loadingalgorithm.LoadingAlgorithmFactory;
import ru.calmsen.loadingparcels.service.parser.JsonTrucksParser;
import ru.calmsen.loadingparcels.service.parser.TxtParcelsParser;
import ru.calmsen.loadingparcels.validator.ParcelValidator;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ParcelsService {
    public static final int TRUCK_WIDTH = 6;
    public static final int TRUCK_HEIGHT = 6;

    private final TxtParcelsParser txtParcelsParser;
    private final JsonTrucksParser jsonTrucksParser;
    private final ParcelValidator parcelValidator;
    private final LoadingAlgorithmFactory loadingAlgorithmFactory;

    public List<Truck> loadParcels(String fileName, LoadingMode loadingMode, int trucksCount) {
        var parcels = txtParcelsParser.parseParcelsFromFile(fileName);
        validateParcels(parcels);

        var loadingAlgorithm = loadingAlgorithmFactory.Create(loadingMode);
        return loadingAlgorithm.loadBoxes(parcels, TRUCK_WIDTH, TRUCK_HEIGHT, trucksCount);
    }

    public List<Box> unloadTrucks(String fileName) {
        var trucks = jsonTrucksParser.parseTrucksFromFile(fileName);
        return trucks.stream()
                .flatMap(truck -> truck.getBoxes().stream())
                .map(PlacedBox::getBox)
                .toList();
    }

    private void validateParcels(List<Box> parcels) {
        var errors = parcels.stream().flatMap(x -> parcelValidator.validate(x).stream()).toList();
        if (!errors.isEmpty()) {
            throw new ParcelValidatorException("Не валидная посылка: " + errors.stream().reduce("\n", (a, b) -> a + "\n" + b));
        }
    }
}
