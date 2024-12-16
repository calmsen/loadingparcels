package calmsen.model.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
public class Truck {
    private final int width = 6;
    private final int height = 6;
    private final List<Parcel> parcels;

    public Truck(Parcel parcel) {
        this.parcels = Arrays.asList(parcel);
    }

    public Truck(List<Parcel> parcels) {
        this.parcels = parcels;
    }
}
