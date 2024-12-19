package ru.calmsen.service;


import ru.calmsen.exception.ParcelValidatorException;
import ru.calmsen.service.loadingalgorithm.LoadingAlgorithmFactory;
import ru.calmsen.model.domain.Box;
import ru.calmsen.model.domain.Truck;
import ru.calmsen.model.domain.enums.LoadingMode;
import ru.calmsen.service.parser.ParcelsParser;
import ru.calmsen.validator.ParcelValidator;
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
