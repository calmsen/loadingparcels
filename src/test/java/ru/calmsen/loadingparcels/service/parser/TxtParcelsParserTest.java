package ru.calmsen.loadingparcels.service.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.util.FileReader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TxtParcelsParserTest {

    @Mock
    private FileReader fileReader;

    @InjectMocks
    private TxtParcelsParser parser;

    @Test
    void shouldReturnEmptyListWhenFileIsEmpty() {
        // Arrange
        when(fileReader.readAllLines(anyString())).thenReturn(List.of());

        // Act
        List<Parcel> result = parser.parseParcelsFromFile("empty_file.txt");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void shouldParseSingleParcelCorrectly() {
        // Arrange
        when(fileReader.readAllLines(anyString()))
                .thenReturn(List.of("8888", "8888"));

        // Act
        List<Parcel> result = parser.parseParcelsFromFile("single_parcel_file.txt");

        // Assert
        assertThat(result).hasSize(1);
        Parcel parcel = result.get(0);
        assertThat(parcel.getContent()).containsExactlyInAnyOrder(
                List.of('8', '8', '8', '8'),
                List.of('8', '8', '8', '8')
        );
    }

    @Test
    void shouldParseMultipleParcelsCorrectly() {
        // Arrange
        when(fileReader.readAllLines(anyString()))
                .thenReturn(List.of("55555", "", "666", "666", "", "777", "7777"));

        // Act
        List<Parcel> result = parser.parseParcelsFromFile("multiple_parcels_file.txt");

        // Assert
        assertThat(result).hasSize(3);
        Parcel firstParcel = result.get(0);
        assertThat(firstParcel.getContent()).containsExactlyInAnyOrder(
                List.of('5', '5', '5', '5', '5')
        );
        Parcel secondParcel = result.get(1);
        assertThat(secondParcel.getContent()).containsExactlyInAnyOrder(
                List.of('6', '6', '6'),
                List.of('6', '6', '6')
        );
        Parcel thirdParcel = result.get(2);
        assertThat(thirdParcel.getContent()).containsExactlyInAnyOrder(
                List.of('7', '7', '7'),
                List.of('7', '7', '7', '7')
        );
    }

    @Test
    void shouldParseInvalidDataAndContinueParsing() {
        // Arrange
        String fileContent = """
                333
                
                invalid
                parcel
                
                4444""";
        when(fileReader.readAllLines(anyString()))
                .thenReturn(List.of("333", "", "invalid", "parcel", "", "4444"));

        // Act
        List<Parcel> result = parser.parseParcelsFromFile("file_with_invalid_data.txt");

        // Assert
        assertThat(result).hasSize(3);
        Parcel firstParcel = result.get(0);
        assertThat(firstParcel.getContent()).containsExactlyInAnyOrder(
                List.of('3', '3', '3')
        );
        Parcel secondParcel = result.get(1);
        assertThat(secondParcel.getContent()).containsExactlyInAnyOrder(
                List.of('i', 'n', 'v', 'a', 'l', 'i', 'd'),
                List.of('p', 'a', 'r', 'c', 'e', 'l')
        );
        Parcel thirdParcel = result.get(2);
        assertThat(thirdParcel.getContent()).containsExactlyInAnyOrder(
                List.of('4', '4', '4', '4')
        );
    }
}