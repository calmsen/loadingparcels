package ru.calmsen.loadingparcels.service.loadingalgorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class OneParcelLoadingAlgorithmTest {

    private OneParcelLoadingAlgorithm algorithm;

    @BeforeEach
    void setUp_LoadingAlgorithm() {
        algorithm = new OneParcelLoadingAlgorithm();
    }

    @Test
    void getMode_ReturnsOneParcelMode() {
        // Arrange & Act
        var mode = algorithm.getMode();

        // Assert
        assertThat(mode).isEqualTo(LoadingMode.ONEPARCEL);
    }

    @Test
    void loadParcels_SufficientTrucks_AssignsOneParcelPerTruck() {
        // Arrange
        var parcelContent = List.of(
                List.of('1')
        );
        var parcels = List.of(
                new Parcel(parcelContent),
                new Parcel(parcelContent),
                new Parcel(parcelContent)
        );
        int truckWidth = 10;
        int truckHeight = 10;
        int trucksCount = 3;

        var trucks = LoadingAlgorithmTestHelper.createTrucks(trucksCount, truckWidth, truckHeight);

        // Act
        algorithm.loadParcels(parcels, trucks);
        var result = trucks.stream().filter(x -> !x.isEmpty()).toList();

        // Assert
        assertThat(result).hasSize(3);

        var truck = result.get(0);
        assertThat(truck.getParcels()).hasSize(1);
        var placedParcel = truck.getParcels().getFirst();
        assertThat(placedParcel.getParcel()).isEqualTo(parcels.get(0));

        truck = result.get(1);
        assertThat(truck.getParcels()).hasSize(1);
        placedParcel = truck.getParcels().getFirst();
        assertThat(placedParcel.getParcel()).isEqualTo(parcels.get(1));

        truck = result.get(2);
        assertThat(truck.getParcels()).hasSize(1);
        placedParcel = truck.getParcels().getFirst();
        assertThat(placedParcel.getParcel()).isEqualTo(parcels.get(2));
    }

    @Test
    void loadParcels_InsufficientTrucks_ThrowsBusinessException() {
        // Arrange
        var parcelContent = List.of(
                List.of('1')
        );
        var parcels = List.of(
                new Parcel(parcelContent),
                new Parcel(parcelContent),
                new Parcel(parcelContent)
        );
        int truckWidth = 10;
        int truckHeight = 10;
        int trucksCount = 2;

        var trucks = LoadingAlgorithmTestHelper.createTrucks(trucksCount, truckWidth, truckHeight);

        // Act & Assert
        Throwable thrown = catchThrowable(() -> algorithm.loadParcels(parcels, trucks));
        var result = trucks.stream().filter(x -> !x.isEmpty()).toList();

        // Assert
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessageContaining("Не достаточно машин для погрузки");
    }

    @Test
    void loadParcels_NoParcels_ReturnsEmptyList() {
        // Arrange
        ArrayList<Parcel> parcels = new ArrayList<>();
        int truckWidth = 10;
        int truckHeight = 10;
        int trucksCount = 0;

        var trucks = LoadingAlgorithmTestHelper.createTrucks(trucksCount, truckWidth, truckHeight);

        // Act
        algorithm.loadParcels(parcels, trucks);
        var result = trucks.stream().filter(x -> !x.isEmpty()).toList();

        // Assert
        assertThat(result).isEmpty();
    }
}