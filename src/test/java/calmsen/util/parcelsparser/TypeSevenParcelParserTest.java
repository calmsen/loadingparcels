package calmsen.util.parcelsparser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import calmsen.exception.ParcelParserException;
import calmsen.model.domain.Parcel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class TypeSevenParcelParserTest {

    private final TypeSevenParcelParser parser = new TypeSevenParcelParser();

    @Test
    void parse_validInput_returnsCorrectParcel() throws Exception {
        // Arrange
        List<String> inputLines = new ArrayList<>();
        inputLines.add("777");
        inputLines.add("7777");

        Iterator<String> iterator = inputLines.iterator();

        // Act
        Parcel parcel = parser.parse(iterator, iterator.next());

        // Assert
        assertNotNull(parcel);
        assertEquals(7, parcel.getType());
        assertEquals(4, parcel.getWidth());
        assertEquals(3, parcel.getTopWidth());
        assertEquals(2, parcel.getHeight());
    }

    @Test
    void parse_noSecondLine_throwsParcelParserException() {
        // Arrange
        List<String> inputLines = new ArrayList<>();
        inputLines.add("777");

        Iterator<String> iterator = inputLines.iterator();

        // Act & Assert
        ParcelParserException exception = assertThrows(ParcelParserException.class,
                () -> parser.parse(iterator, iterator.next()));
        assertTrue(exception.getMessage().contains("поток строк завершился"));
    }

    @Test
    void parse_wrongSecondLine_throwsParcelParserException() {
        // Arrange
        List<String> inputLines = new ArrayList<>();
        inputLines.add("777");
        inputLines.add("8888");

        Iterator<String> iterator = inputLines.iterator();

        // Act & Assert
        ParcelParserException exception = assertThrows(ParcelParserException.class,
                () -> parser.parse(iterator, iterator.next()));
        assertTrue(exception.getMessage().contains("Текущая строка: 8888. Ожидалось: 7777"));
    }

    @Test
    void parse_wrongFirstLine_returnsNull() {
        // Arrange
        List<String> inputLines = new ArrayList<>();
        inputLines.add("888");
        inputLines.add("777");

        Iterator<String> iterator = inputLines.iterator();

        // Act
        Parcel result = parser.parse(iterator, iterator.next());

        // Assert
        assertNull(result);
    }
}