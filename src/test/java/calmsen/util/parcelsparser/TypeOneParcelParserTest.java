package calmsen.util.parcelsparser;

import calmsen.model.domain.Parcel;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeOneParcelParserTest {

    @Test
    void parse_CurrentLineIsOne_ResultCorrectParcel() {
        // Arrange
        Iterator<String> iterator = mockIterator("1");
        String currentLine = "1";
        TypeOneParcelParser parser = new TypeOneParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertEquals(1, result.getType());
        assertEquals(1, result.getWidth());
        assertEquals(1, result.getTopWidth());
        assertEquals(1, result.getHeight());
    }

    @Test
    void parse_CurrentLineIsEmpty_ResultNull() {
        // Arrange
        Iterator<String> iterator = mockIterator("");
        String currentLine = "";
        TypeOneParcelParser parser = new TypeOneParcelParser();

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
        TypeOneParcelParser parser = new TypeOneParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertNull(result);
    }

    @Test
    void parse_CurrentLineIsAnotherNumber_ResultNull() {
        // Arrange
        Iterator<String> iterator = mockIterator("2");
        String currentLine = "2";
        TypeOneParcelParser parser = new TypeOneParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertNull(result);
    }

    @Test
    void parse_CurrentLineIsOneAndOtherLinesExist_ResultCorrectParcel() {
        // Arrange
        Iterator<String> iterator = mockIterator("1", "other line");
        String currentLine = "1";
        TypeOneParcelParser parser = new TypeOneParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertEquals(1, result.getType());
        assertEquals(1, result.getWidth());
        assertEquals(1, result.getTopWidth());
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