package calmsen.service;


import calmsen.model.domain.Parcel;
import calmsen.model.domain.Truck;
import calmsen.model.domain.enums.LoadingMode;
import calmsen.util.parcelsparser.ParcelsParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ParcelsService {
    private final ParcelsParser parcelsParser;

    public List<Truck> loadParcels(String filePath, LoadingMode loadingMode) {
        var parcels = parcelsParser.parseParcelsFromFile(filePath);
        if (loadingMode == LoadingMode.SIMPLE){
            return simpleLoadParcels(parcels);
        }

        if (loadingMode == LoadingMode.EFFICIENT){
            return efficientLoadParcels(parcels);
        }

        throw new IllegalArgumentException("Unsupported loading mode: " + loadingMode);
    }

    private static List<Truck> simpleLoadParcels(List<Parcel> parcels) {
        return parcels.stream().map(Truck::new).collect(Collectors.toList());
    }

    private static List<Truck> efficientLoadParcels(List<Parcel> parcels) {
        return parcels.stream().map(Truck::new).collect(Collectors.toList());
    }
}
