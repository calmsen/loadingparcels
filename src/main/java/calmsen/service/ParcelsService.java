package calmsen.service;


import calmsen.exception.ParcelValidatorException;
import calmsen.loadingalgorithm.LoadingAlgorithmFactory;
import calmsen.model.domain.Box;
import calmsen.model.domain.Truck;
import calmsen.model.domain.enums.LoadingMode;
import calmsen.parser.ParcelsParser;
import calmsen.validator.ParcelValidator;
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
        for (var parcel : parcels) {
            if (!parcelValidator.validate(parcel)) {
                throw new ParcelValidatorException("Не валидная посылка: \n" + parcel.toString());
            }
        }
        var loadingAlgorithm = loadingAlgorithmFactory.Create(loadingMode);
        return loadingAlgorithm.loadBoxes(
                parcels.stream().map(x -> (Box)x).toList(),
                TRUCK_WIDTH, TRUCK_HEIGHT);
    }
}
