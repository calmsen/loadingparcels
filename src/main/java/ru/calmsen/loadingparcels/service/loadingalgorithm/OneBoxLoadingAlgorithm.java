package ru.calmsen.loadingparcels.service.loadingalgorithm;

import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.Box;
import ru.calmsen.loadingparcels.model.domain.PlacedBox;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Погрузка по одной посылке.
 */
@Slf4j
public class OneBoxLoadingAlgorithm implements LoadingAlgorithm {
    private final LoadingMode mode = LoadingMode.ONEBOX;

    @Override
    public LoadingMode getMode() {
        return this.mode;
    }

    @Override
    public List<Truck> loadBoxes(List<Box> boxes, int truckWidth, int truckHeight, int trucksCount) {
        if (trucksCount > boxes.size()) {
            throw new BusinessException("Не достаточно машин для погрузки. Минимальное количество: " + boxes.size());
        }
        return boxes.stream().map(x -> new Truck(truckWidth, truckHeight, new PlacedBox(x))).collect(Collectors.toList());
    }
}
