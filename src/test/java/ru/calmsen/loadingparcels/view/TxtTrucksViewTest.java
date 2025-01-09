package ru.calmsen.loadingparcels.view;

import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.PlacedParcel;
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
        var parcel = new Parcel(List.of(
                List.of('9', '9', '9'),
                List.of('9', '9', '9'),
                List.of('9', '9', '9')
        ));
        truck.loadParcel(new PlacedParcel(parcel, 0, 0));
        parcel = new Parcel(List.of(
                List.of('3', '3', '3')
        ));
        truck.loadParcel(new PlacedParcel(parcel, 3, 0));
        var view = new TxtTrucksView();
        var result = view.getOutputData(List.of(truck));

        var expectedOutput = String.join("\n",
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
        Parcel parcel1 = new Parcel(List.of(
                List.of('2', '2')
        ));

        var parcel2 = new Parcel(List.of(
                List.of('6', '6', '6'),
                List.of('6', '6', '6')
        ));

        var trucks = List.of(
                new Truck(6, 6, new PlacedParcel(parcel1)),
                new Truck(6, 6, new PlacedParcel(parcel2))
        );
        var view = new TxtTrucksView();
        var result = view.getOutputData(trucks);

        var expectedOutput = String.join("\n",
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