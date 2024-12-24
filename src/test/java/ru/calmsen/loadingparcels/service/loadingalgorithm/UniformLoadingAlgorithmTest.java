package ru.calmsen.loadingparcels.service.loadingalgorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.domain.Box;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class UniformLoadingAlgorithmTest {

    private UniformLoadingAlgorithm algorithm;

    @BeforeEach
    void setUp_LoadingAlgorithm() {
        algorithm = new UniformLoadingAlgorithm();
    }

    @Test
    void loadBoxes_EnoughTrucks_LoadsBoxesSuccessfully() {
        // Arrange
        var boxes = List.of(
                new Box(List.of(
                        List.of('1')
                )),
                new Box(List.of(
                        List.of('6', '6', '6'),
                        List.of('6', '6', '6')
                )),
                new Box(List.of(
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9')
                ))
        );
        int truckWidth = 3;
        int truckHeight = 3;
        int trucksCount = 2;

        // Act
        var result = algorithm.loadBoxes(boxes, truckWidth, truckHeight, trucksCount);

        // Assert
        assertThat(result).hasSize(2);
        var truck = result.get(0);
        assertThat(truck.getBoxes()).hasSize(1);
        truck = result.get(1);
        assertThat(truck.getBoxes()).hasSize(2);
    }

    @Test
    void loadBoxes_NotEnoughTrucks_ThrowsBusinessException() {
        // Arrange

        var boxes = List.of(
                new Box(List.of(
                        List.of('6', '6', '6'),
                        List.of('6', '6', '6')
                )),
                new Box(List.of(
                        List.of('6', '6', '6'),
                        List.of('6', '6', '6')
                )),
                new Box(List.of(
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9')
                ))
        );
        int truckWidth = 3;
        int truckHeight = 3;
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
        int truckWidth = 6;
        int truckHeight = 6;
        int trucksCount = 0;

        // Act
        var result = algorithm.loadBoxes(List.of(), truckWidth, truckHeight, trucksCount);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void loadBoxes_TwoBoxesFitInOneTruck_LoadsTwoBoxesIntoOneTruck() {
        // Arrange
        var boxes = List.of(
                new Box(List.of(
                        List.of('6', '6', '6'),
                        List.of('6', '6', '6')
                )),
                new Box(List.of(
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9')
                ))
        );
        int truckWidth = 6;
        int truckHeight = 6;
        int trucksCount = 1;

        // Act
        var result = algorithm.loadBoxes(boxes, truckWidth, truckHeight, trucksCount);

        // Assert
        assertThat(result).hasSize(1);
        var truck = result.getFirst();
        assertThat(truck.getBoxes()).hasSize(2);
        var placedBox = truck.getBoxes().get(0);
        assertThat(placedBox.getBox()).isEqualTo(boxes.get(1));
        assertThat(placedBox.getPositionX()).isEqualTo(0);
        assertThat(placedBox.getPositionY()).isEqualTo(0);

        placedBox = truck.getBoxes().get(1);
        assertThat(placedBox.getBox()).isEqualTo(boxes.get(0));

        assertThat(placedBox.getPositionX()).isEqualTo(3);
        assertThat(placedBox.getPositionY()).isEqualTo(0);
    }

    @Test
    void loadBoxes_UniformLoading_LoadsFourSmallBoxesIntoSecondTruck() {
        // Arrange
        var boxes = List.of(
                new Box(List.of(
                        List.of('6', '6', '6'),
                        List.of('6', '6', '6')
                )),
                new Box(List.of(
                        List.of('1')
                )),
                new Box(List.of(
                        List.of('1')
                )),
                new Box(List.of(
                        List.of('1')
                )),
                new Box(List.of(
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9'),
                        List.of('9', '9', '9')
                ))
        );
        int truckWidth = 6;
        int truckHeight = 6;
        int trucksCount = 2;

        // Act
        var result = algorithm.loadBoxes(boxes, truckWidth, truckHeight, trucksCount);

        // Assert
        assertThat(result).hasSize(2);
        var truck = result.getFirst();
        assertThat(truck.getBoxes()).hasSize(1);

        truck = result.get(1);
        assertThat(truck.getBoxes()).hasSize(4);
    }
}