package ru.calmsen.loadingparcels.service;


import ru.calmsen.loadingparcels.exception.ParcelValidatorException;
import ru.calmsen.loadingparcels.service.loadingalgorithm.LoadingAlgorithmFactory;
import ru.calmsen.loadingparcels.domain.Box;
import ru.calmsen.loadingparcels.domain.Truck;
import ru.calmsen.loadingparcels.domain.enums.LoadingMode;
import ru.calmsen.loadingparcels.service.parser.ParcelsParser;
import ru.calmsen.loadingparcels.validator.ParcelValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ParcelsService {
    public static final int TRUCK_WIDTH = 6;
    public static final int TRUCK_HEIGHT = 6;

    private final ParcelsParser parcelsParser;
    private final ParcelValidator parcelValidator;
    private final LoadingAlgorithmFactory loadingAlgorithmFactory;

    public List<Truck> loadParcels(String filePath, LoadingMode loadingMode) {
        var parcels = parcelsParser.parseParcelsFromFile(filePath);
        validateParcels(parcels);

        var loadingAlgorithm = loadingAlgorithmFactory.Create(loadingMode);
        return loadingAlgorithm.loadBoxes(parcels, TRUCK_WIDTH, TRUCK_HEIGHT);
    }

    private void validateParcels(List<Box> parcels) {
        var errors = parcels.stream().flatMap(x -> parcelValidator.validate(x).stream()).toList();
        if (!errors.isEmpty()) {
            throw new ParcelValidatorException("Не валидная посылка: " + errors.stream().reduce("\n", (a, b) -> a + "\n" + b));
        }
    }
}
