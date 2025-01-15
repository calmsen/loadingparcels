package ru.calmsen.loadingparcels.view;

import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.model.domain.Parcel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

class TxtParcelsViewTest {
    @Test
    void buildOutputData_SingleParcel_CorrectlyFormatsOutput() {
        // Arrange
        List<List<Character>> content = List.of(
                List.of('a', 'b'),
                List.of('c', 'd')
        );
        var singleParcel = new Parcel(content);
        List<Parcel> parcels = List.of(singleParcel);

        // Act
        String result = new TxtParcelsView().buildOutputData(parcels);

        // Assert
        assertThat(result).isEqualTo("name: Посылка тип a\ncontent: \nab\ncd"); // Проверяем вывод для одного бокса
    }

    @Test
    void buildOutputData_MultipleParcels_FormatsEachParcelSeparately() {
        // Arrange
        List<List<Character>> content1 = List.of(
                List.of('a', 'b'),
                List.of('c', 'd')
        );
        List<List<Character>> content2 = List.of(
                List.of('e', 'f'),
                List.of('g', 'h')
        );
        Parcel parcel1 = new Parcel(content1);
        Parcel parcel2 = new Parcel(content2);
        List<Parcel> parcels = List.of(parcel1, parcel2);

        // Act
        String result = new TxtParcelsView().buildOutputData(parcels);

        // Assert
        assertThat(result).isEqualTo("name: Посылка тип a\ncontent: \nab\ncd\n\nname: Посылка тип e\ncontent: \nef\ngh"); // Проверяем вывод для нескольких боксов
    }
}