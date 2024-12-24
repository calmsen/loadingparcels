package ru.calmsen.loadingparcels.service.parser;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.calmsen.loadingparcels.mapper.TrucksMapperImpl;
import ru.calmsen.loadingparcels.util.FileReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class JsonTrucksParserTest {

    @Test
    void parseTrucksFromFile_parseOneTruckWithBox_returnOneTruckWithBox() {
        // Arrange
        var fileReader = Mockito.mock(FileReader.class);
        when(fileReader.readString("test.json"))
                .thenReturn("[{\"width\":8,\"height\":8,\"boxes\":[{\"box\":{\"width\":3,\"height\":3,\"dimensions\":9},\"positionX\":3,\"positionY\":3}]}]");
        var parser = new JsonTrucksParser(fileReader, new TrucksMapperImpl());

        // Act
        var result = parser.parseTrucksFromFile("test.json");

        // Assert
        assertThat(result).isNotNull().hasSize(1);
        var truck = result.getFirst();
        assertThat(truck).isNotNull();
        assertThat(truck.getWidth()).isEqualTo(8);
        assertThat(truck.getHeight()).isEqualTo(8);

        var boxes = truck.getBoxes();
        assertThat(boxes).isNotNull().hasSize(1);
        var box = boxes.getFirst();
        assertThat(box).isNotNull();
        assertThat(box.getPositionX()).isEqualTo(3);
        assertThat(box.getPositionY()).isEqualTo(3);

        var boxDetails = box.getBox();
        assertThat(boxDetails).isNotNull();
        assertThat(boxDetails.getWidth()).isEqualTo(3);
        assertThat(boxDetails.getHeight()).isEqualTo(3);
        assertThat(boxDetails.getDimensions()).isEqualTo(9);
    }
}