package calmsen.loadingalgorithm;

import calmsen.model.domain.Parcel;
import calmsen.model.domain.Truck;
import calmsen.model.domain.enums.LoadingMode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SimpleLoadingAlgorithm extends LoadingAlgorithm {
    private final LoadingMode mode = LoadingMode.SIMPLE;
    @Override
    public LoadingMode getMode() {
        return this.mode;
    }

    @Override
    public List<Truck> loadParcels(List<Parcel> parcels, int truckWidth, int truckHeight) {
        return parcels.stream().map(x -> new Truck(truckWidth, truckHeight, x)).collect(Collectors.toList());
    }
}
