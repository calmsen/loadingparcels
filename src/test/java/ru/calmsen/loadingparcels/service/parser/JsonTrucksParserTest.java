package ru.calmsen.loadingparcels.service.parser;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.calmsen.loadingparcels.mapper.TrucksMapperImpl;
import ru.calmsen.loadingparcels.util.FileReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class JsonTrucksParserTest {

    @Test
    void parseTrucksFromFile_parseOneTruckWithParcel_returnOneTruckWithParcel() {
        // Arrange
        var fileReader = Mockito.mock(FileReader.class);
        when(fileReader.readString("test.json"))
                .thenReturn("[{\"width\":8,\"height\":8,\"parcels\":[{\"parcel\":{\"width\":3,\"height\":3,\"dimensions\":9,\"form\":\"xxx\nxxx\nxxx\"},\"positionX\":3,\"positionY\":3}]}]");
        var parser = new JsonTrucksParser(fileReader, new TrucksMapperImpl());

        // Act
        var result = parser.parseTrucksFromFile("test.json");

        // Assert
        assertThat(result).isNotNull().hasSize(1);
        var truck = result.getFirst();
        assertThat(truck).isNotNull();
        assertThat(truck.getWidth()).isEqualTo(8);
        assertThat(truck.getHeight()).isEqualTo(8);

        var parcels = truck.getParcels();
        assertThat(parcels).isNotNull().hasSize(1);
        var parcel = parcels.getFirst();
        assertThat(parcel).isNotNull();
        assertThat(parcel.getPositionX()).isEqualTo(3);
        assertThat(parcel.getPositionY()).isEqualTo(3);

        var parcelDetails = parcel.getParcel();
        assertThat(parcelDetails).isNotNull();
        assertThat(parcelDetails.getWidth()).isEqualTo(3);
        assertThat(parcelDetails.getHeight()).isEqualTo(3);
        assertThat(parcelDetails.getDimensions()).isEqualTo(9);
    }
}