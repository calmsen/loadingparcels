package calmsen.util.parcelsparser;

import calmsen.model.domain.Parcel;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeFourParcelParserTest {

    @Test
    void parse_CurrentLineIsFourFourFour_ResultCorrectParcel() {
        // Arrange
        Iterator<String> iterator = mockIterator("4444");
        String currentLine = "4444";
        TypeFourParcelParser parser = new TypeFourParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertEquals(4, result.getType());
        assertEquals(4, result.getWidth());
        assertEquals(4, result.getTopWidth());
        assertEquals(1, result.getHeight());
    }

    @Test
    void parse_CurrentLineIsEmpty_ResultNull() {
        // Arrange
        Iterator<String> iterator = mockIterator("");
        String currentLine = "";
        TypeFourParcelParser parser = new TypeFourParcelParser();

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
        TypeFourParcelParser parser = new TypeFourParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertNull(result);
    }

    @Test
    void parse_CurrentLineIsAnotherNumber_ResultNull() {
        // Arrange
        Iterator<String> iterator = mockIterator("5555");
        String currentLine = "5555";
        TypeFourParcelParser parser = new TypeFourParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertNull(result);
    }

    @Test
    void parse_CurrentLineIsFourFourFourAndOtherLinesExist_ResultCorrectParcel() {
        // Arrange
        Iterator<String> iterator = mockIterator("4444", "other line");
        String currentLine = "4444";
        TypeFourParcelParser parser = new TypeFourParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertEquals(4, result.getType());
        assertEquals(4, result.getWidth());
        assertEquals(4, result.getTopWidth());
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