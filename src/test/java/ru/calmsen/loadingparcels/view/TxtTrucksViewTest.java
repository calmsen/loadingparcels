package ru.calmsen.loadingparcels.view;

import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.model.domain.Box;
import ru.calmsen.loadingparcels.model.domain.PlacedBox;
import ru.calmsen.loadingparcels.model.domain.Truck;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TxtTrucksViewTest {
    @Test
    void shouldShowEmptyTruck() {
        var view = new TxtTrucksView();
        var trucks = List.of(new Truck(6, 6));
        var result = view.getOutputData(trucks);

        var expectedOutput = String.join("\n",
                "\n",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "++++++++"
        );

        assertEquals(expectedOutput, result);
    }

    @Test
    void shouldShowLoadedTruck() {
        var truck = new Truck(6, 6);
        var box = new Box(List.of(
                List.of('9', '9', '9'),
                List.of('9', '9', '9'),
                List.of('9', '9', '9')
        ));
        truck.loadBox(new PlacedBox(box, 0, 0));
        box = new Box(List.of(
                List.of('3', '3', '3')
        ));
        truck.loadBox(new PlacedBox(box, 3, 0));
        var view = new TxtTrucksView();
        var result = view.getOutputData(List.of(truck));

        var expectedOutput = String.join("\n",
                "\n",
                "+      +",
                "+      +",
                "+      +",
                "+999   +",
                "+999   +",
                "+999333+",
                "++++++++"
        );

        assertEquals(expectedOutput, result);
    }

    @Test
    void shouldShowMultipleTrucks() {
        Box box1 = new Box(List.of(
                List.of('2', '2')
        ));

        var box2 = new Box(List.of(
                List.of('6', '6', '6'),
                List.of('6', '6', '6')
        ));

        var trucks = List.of(
                new Truck(6, 6, new PlacedBox(box1)),
                new Truck(6, 6, new PlacedBox(box2))
        );
        var view = new TxtTrucksView();
        var result = view.getOutputData(trucks);

        var expectedOutput = String.join("\n",
                "\n",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "+22    +",
                "++++++++",
                "",
                "+      +",
                "+      +",
                "+      +",
                "+      +",
                "+666   +",
                "+666   +",
                "++++++++"
        );

        assertEquals(expectedOutput, result);
    }
}