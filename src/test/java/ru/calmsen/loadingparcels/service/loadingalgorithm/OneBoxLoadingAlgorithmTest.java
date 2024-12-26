package ru.calmsen.loadingparcels.service.loadingalgorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.model.domain.Box;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class OneBoxLoadingAlgorithmTest {

    private OneBoxLoadingAlgorithm algorithm;

    @BeforeEach
    void setUp_LoadingAlgorithm() {
        algorithm = new OneBoxLoadingAlgorithm();
    }

    @Test
    void getMode_ReturnsOneBoxMode() {
        // Arrange & Act
        var mode = algorithm.getMode();

        // Assert
        assertThat(mode).isEqualTo(LoadingMode.ONEBOX);
    }

    @Test
    void loadBoxes_SufficientTrucks_AssignsOneBoxPerTruck() {
        // Arrange
        var boxContent = List.of(
                List.of('1')
        );
        var boxes = List.of(
                new Box(boxContent),
                new Box(boxContent),
                new Box(boxContent)
        );
        int truckWidth = 10;
        int truckHeight = 10;
        int trucksCount = 3;

        // Act
        var result = algorithm.loadBoxes(boxes, truckWidth, truckHeight, trucksCount);

        // Assert
        assertThat(result).hasSize(3);

        var truck = result.get(0);
        assertThat(truck.getBoxes()).hasSize(1);
        var placedBox = truck.getBoxes().getFirst();
        assertThat(placedBox.getBox()).isEqualTo(boxes.get(0));

        truck = result.get(1);
        assertThat(truck.getBoxes()).hasSize(1);
        placedBox = truck.getBoxes().getFirst();
        assertThat(placedBox.getBox()).isEqualTo(boxes.get(1));

        truck = result.get(2);
        assertThat(truck.getBoxes()).hasSize(1);
        placedBox = truck.getBoxes().getFirst();
        assertThat(placedBox.getBox()).isEqualTo(boxes.get(2));
    }

    @Test
    void loadBoxes_InsufficientTrucks_ThrowsBusinessException() {
        // Arrange
        var boxContent = List.of(
                List.of('1')
        );
        var boxes = List.of(
                new Box(boxContent),
                new Box(boxContent),
                new Box(boxContent)
        );
        int truckWidth = 10;
        int truckHeight = 10;
        int trucksCount = 2;

        // Act & Assert
        Throwable thrown = catchThrowable(() -> algorithm.loadBoxes(boxes, truckWidth, truckHeight, trucksCount));

        // Assert
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessageContaining("Не достаточно машин для погрузки.");
    }

    @Test
    void loadBoxes_NoBoxes_ReturnsEmptyList() {
        // Arrange
        var boxes = new ArrayList<Box>();
        int truckWidth = 10;
        int truckHeight = 10;
        int trucksCount = 0;

        // Act
        var result = algorithm.loadBoxes(boxes, truckWidth, truckHeight, trucksCount);

        // Assert
        assertThat(result).isEmpty();
    }
}