package ru.calmsen.loadingparcels.view;

import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.model.domain.Parcel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvWithCountParcelsViewTest {
    @Test
    void getOutputData_SingleParcel_CorrectlyFormatsOutput() {
        // Arrange
        List<List<Character>> content = List.of(
                List.of('a', 'b'),
                List.of('c', 'd')
        );
        var singleParcel = new Parcel(content);
        List<Parcel> parcels = List.of(singleParcel);

        // Act
        String result = new CsvWithCountParcelsView().getOutputData(parcels);

        // Assert
        assertEquals("Имя;Форма;Символ;Количество\nПосылка тип a;xx\\nxx;a;1", result); // Проверяем вывод для одного бокса
    }

    @Test
    void getOutputData_MultipleParcels_FormatsEachParcelSeparately() {
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
        String result = new CsvWithCountParcelsView().getOutputData(parcels);

        // Assert
        assertEquals("Имя;Форма;Символ;Количество\nПосылка тип e;xx\\nxx;e;1\nПосылка тип a;xx\\nxx;a;1", result); // Проверяем вывод для нескольких боксов
    }
}