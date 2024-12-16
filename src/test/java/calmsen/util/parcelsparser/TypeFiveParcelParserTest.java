package calmsen.util.parcelsparser;

import calmsen.model.domain.Parcel;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeFiveParcelParserTest {

    @Test
    void parse_CurrentLineIsFiveFiveFive_ResultCorrectParcel() {
        // Arrange
        Iterator<String> iterator = mockIterator("55555");
        String currentLine = "55555";
        TypeFiveParcelParser parser = new TypeFiveParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertEquals(5, result.getType());
        assertEquals(5, result.getWidth());
        assertEquals(5, result.getTopWidth());
        assertEquals(1, result.getHeight());
    }

    @Test
    void parse_CurrentLineIsEmpty_ResultNull() {
        // Arrange
        Iterator<String> iterator = mockIterator("");
        String currentLine = "";
        TypeFiveParcelParser parser = new TypeFiveParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertNull(result);
    }

    @Test
    void parse_CurrentLineContainsSpacesOnly_ResultNull() {
        // Arrange
        Iterator<String> iterator = mockIterator("   ");
        String currentLine = "   ";
        TypeFiveParcelParser parser = new TypeFiveParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertNull(result);
    }

    @Test
    void parse_CurrentLineIsAnotherNumber_ResultNull() {
        // Arrange
        Iterator<String> iterator = mockIterator("66666");
        String currentLine = "66666";
        TypeFiveParcelParser parser = new TypeFiveParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertNull(result);
    }

    @Test
    void parse_CurrentLineIsFiveFiveFiveAndOtherLinesExist_ResultCorrectParcel() {
        // Arrange
        Iterator<String> iterator = mockIterator("55555", "other line");
        String currentLine = "55555";
        TypeFiveParcelParser parser = new TypeFiveParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertEquals(5, result.getType());
        assertEquals(5, result.getWidth());
        assertEquals(5, result.getTopWidth());
        assertEquals(1, result.getHeight());
    }

    /**
     * Создание мнимого итератора для тестирования.
     */
    private Iterator<String> mockIterator(String... strings) {
        return new Iterator<String>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < strings.length;
            }

            @Override
            public String next() {
                return strings[index++];
            }
        };
    }
}