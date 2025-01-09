package ru.calmsen.loadingparcels.service.loadingalgorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.Truck;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class EfficientLoadingAlgorithmTest {

    private EfficientLoadingAlgorithm algorithm;

    @BeforeEach
    void setUp_LoadingAlgorithm() {
        algorithm = new EfficientLoadingAlgorithm();
    }

    @Test
    void loadParcels_EnoughTrucks_LoadsParcelsSuccessfully() {
        // Arrange
        var parcels = List.of(
                new Parcel(List.of(
                        List.of('1')
                )),
                new Parcel(List.of(
                        List.of('6', '6', '6'),
                        List.of('6', '6', '6')
                )),
                new Parcel(List.of(
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9')
                ))
        );
        int truckWidth = 3;
        int truckHeight = 3;
        int trucksCount = 2;

        List<Truck> trucks = LoadingAlgorithmTestHelper.createTrucks(trucksCount, truckWidth, truckHeight);

        // Act
        algorithm.loadParcels(parcels, trucks);
        var result = trucks.stream().filter(x -> !x.isEmpty()).toList();

                // Assert
        assertThat(result).hasSize(2);
        var truck = result.get(0);
        assertThat(truck.getParcels()).hasSize(1);
        truck = result.get(1);
        assertThat(truck.getParcels()).hasSize(2);
    }

    @Test
    void loadParcels_NotEnoughTrucks_ThrowsBusinessException() {
        // Arrange

        var parcels = List.of(
                new Parcel(List.of(
                        List.of('6', '6', '6'),
                        List.of('6', '6', '6')
                )),
                new Parcel(List.of(
                        List.of('6', '6', '6'),
                        List.of('6', '6', '6')
                )),
                new Parcel(List.of(
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9')
                ))
        );
        int truckWidth = 3;
        int truckHeight = 3;
        int trucksCount = 2;

        List<Truck> trucks = LoadingAlgorithmTestHelper.createTrucks(trucksCount, truckWidth, truckHeight);

        // Act & Assert
        Throwable thrown = catchThrowable(() -> algorithm.loadParcels(parcels, trucks));

        // Assert
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessageContaining("Не достаточно машин для погрузки");
    }

    @Test
    void loadParcels_NoParcels_ReturnsEmptyList() {
        // Arrange
        int truckWidth = 6;
        int truckHeight = 6;
        int trucksCount = 0;

        List<Truck> trucks = LoadingAlgorithmTestHelper.createTrucks(trucksCount, truckWidth, truckHeight);

        // Act
        algorithm.loadParcels(List.of(), trucks);
        var result = trucks.stream().filter(x -> !x.isEmpty()).toList();

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void loadParcels_TwoParcelsFitInOneTruck_LoadsTwoParcelsIntoOneTruck() {
        // Arrange
        var parcels = List.of(
                new Parcel(List.of(
                        List.of('6', '6', '6'),
                        List.of('6', '6', '6')
                )),
                new Parcel(List.of(
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9')
                ))
        );
        int truckWidth = 6;
        int truckHeight = 6;
        int trucksCount = 1;

        List<Truck> trucks = LoadingAlgorithmTestHelper.createTrucks(trucksCount, truckWidth, truckHeight);

        // Act
        algorithm.loadParcels(parcels, trucks);
        var result = trucks.stream().filter(x -> !x.isEmpty()).toList();

        // Assert
        assertThat(result).hasSize(1);
        var truck = result.getFirst();
        assertThat(truck.getParcels()).hasSize(2);
        var placedParcel = truck.getParcels().get(0);
        assertThat(placedParcel.getParcel()).isEqualTo(parcels.get(1));
        assertThat(placedParcel.getPositionX()).isEqualTo(0);
        assertThat(placedParcel.getPositionY()).isEqualTo(0);

        placedParcel = truck.getParcels().get(1);
        assertThat(placedParcel.getParcel()).isEqualTo(parcels.get(0));

        assertThat(placedParcel.getPositionX()).isEqualTo(3);
        assertThat(placedParcel.getPositionY()).isEqualTo(0);
    }
}