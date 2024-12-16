package calmsen.util.parcelsparser;

import calmsen.model.domain.Parcel;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeTwoParcelParserTest {

    @Test
    void parse_CurrentLineIsTwentyTwo_ResultCorrectParcel() {
        // Arrange
        Iterator<String> iterator = mockIterator("22");
        String currentLine = "22";
        TypeTwoParcelParser parser = new TypeTwoParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertEquals(2, result.getType());
        assertEquals(2, result.getWidth());
        assertEquals(2, result.getTopWidth());
        assertEquals(1, result.getHeight());
    }

    @Test
    void parse_CurrentLineIsEmpty_ResultNull() {
        // Arrange
        Iterator<String> iterator = mockIterator("");
        String currentLine = "";
        TypeTwoParcelParser parser = new TypeTwoParcelParser();

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
        TypeTwoParcelParser parser = new TypeTwoParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertNull(result);
    }

    @Test
    void parse_CurrentLineIsAnotherNumber_ResultNull() {
        // Arrange
        Iterator<String> iterator = mockIterator("21");
        String currentLine = "21";
        TypeTwoParcelParser parser = new TypeTwoParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertNull(result);
    }

    @Test
    void parse_CurrentLineIsTwentyTwoAndOtherLinesExist_ResultCorrectParcel() {
        // Arrange
        Iterator<String> iterator = mockIterator("22", "other line");
        String currentLine = "22";
        TypeTwoParcelParser parser = new TypeTwoParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertEquals(2, result.getType());
        assertEquals(2, result.getWidth());
        assertEquals(2, result.getTopWidth());
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