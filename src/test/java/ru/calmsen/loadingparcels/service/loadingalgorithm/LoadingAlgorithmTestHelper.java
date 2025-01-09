package ru.calmsen.loadingparcels.service.loadingalgorithm;

import ru.calmsen.loadingparcels.model.domain.Truck;

import java.util.ArrayList;
import java.util.List;

public class LoadingAlgorithmTestHelper {
    public static List<Truck> createTrucks(int trucksCount, int truckWidth, int truckHeight) {
        List<Truck> trucks = new ArrayList<>();
        for (int i = 0; i < trucksCount; i++) {
            trucks.add(new Truck(truckWidth, truckHeight));
        }
        return trucks;
    }
}
