package calmsen.util.parcelsparser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import calmsen.exception.ParcelParserException;
import calmsen.model.domain.Parcel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class TypeNineParcelParserTest {

    private final TypeNineParcelParser parser = new TypeNineParcelParser();

    @Test
    void parse_validInput_returnsCorrectParcel() throws Exception {
        // Arrange
        List<String> inputLines = new ArrayList<>();
        inputLines.add("999");
        inputLines.add("999");
        inputLines.add("999");

        Iterator<String> iterator = inputLines.iterator();

        // Act
        Parcel parcel = parser.parse(iterator, iterator.next());

        // Assert
        assertNotNull(parcel);
        assertEquals(9, parcel.getType());
        assertEquals(3, parcel.getWidth());
        assertEquals(3, parcel.getTopWidth());
        assertEquals(3, parcel.getHeight());
    }

    @Test
    void parse_noThirdLine_throwsParcelParserException() {
        // Arrange
        List<String> inputLines = new ArrayList<>();
        inputLines.add("999");
        inputLines.add("999");

        Iterator<String> iterator = inputLines.iterator();

        // Act & Assert
        ParcelParserException exception = assertThrows(ParcelParserException.class,
                () -> parser.parse(iterator, iterator.next()));
        assertTrue(exception.getMessage().contains("поток строк завершился"));
    }

    @Test
    void parse_invalidSecondLine_throwsParcelParserException() {
        // Arrange
        List<String> inputLines = new ArrayList<>();
        inputLines.add("999");
        inputLines.add("998");
        inputLines.add("999");

        Iterator<String> iterator = inputLines.iterator();

        // Act & Assert
        ParcelParserException exception = assertThrows(ParcelParserException.class,
                () -> parser.parse(iterator, iterator.next()));
        assertTrue(exception.getMessage().contains("Текущая строка: 998. Ожидалось: 999"));
    }

    @Test
    void parse_invalidThirdLine_throwsParcelParserException() {
        // Arrange
        List<String> inputLines = new ArrayList<>();
        inputLines.add("999");
        inputLines.add("999");
        inputLines.add("998");

        Iterator<String> iterator = inputLines.iterator();

        // Act & Assert
        ParcelParserException exception = assertThrows(ParcelParserException.class,
                () -> parser.parse(iterator, iterator.next()));
        assertTrue(exception.getMessage().contains("Текущая строка: 998. Ожидалось: 999"));
    }

    @Test
    void parse_wrongFirstLine_returnsNull() {
        // Arrange
        List<String> inputLines = new ArrayList<>();
        inputLines.add("111");
        inputLines.add("999");

        Iterator<String> iterator = inputLines.iterator();

        // Act
        Parcel result = parser.parse(iterator, iterator.next());

        // Assert
        assertNull(result);
    }
}