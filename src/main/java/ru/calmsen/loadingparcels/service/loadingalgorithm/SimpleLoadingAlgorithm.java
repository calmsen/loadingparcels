package ru.calmsen.loadingparcels.service.loadingalgorithm;

import ru.calmsen.loadingparcels.domain.Box;
import ru.calmsen.loadingparcels.domain.Truck;
import ru.calmsen.loadingparcels.domain.enums.LoadingMode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SimpleLoadingAlgorithm implements LoadingAlgorithm {
    private final LoadingMode mode = LoadingMode.SIMPLE;

    @Override
    public LoadingMode getMode() {
        return this.mode;
    }

    @Override
    public List<Truck> loadBoxes(List<Box> boxes, int truckWidth, int truckHeight) {
        return boxes.stream().map(x -> new Truck(truckWidth, truckHeight, x)).collect(Collectors.toList());
    }
}
