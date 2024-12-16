package calmsen.util.parcelsparser;

import calmsen.model.domain.Parcel;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TypeThreeParcelParserTest {

    @Test
    void parse_CurrentLineIsThree_ResultCorrectParcel() {
        // Arrange
        Iterator<String> iterator = mockIterator("333");
        String currentLine = "333";
        TypeThreeParcelParser parser = new TypeThreeParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertEquals(3, result.getType());
        assertEquals(3, result.getWidth());
        assertEquals(3, result.getTopWidth());
        assertEquals(1, result.getHeight());
    }

    @Test
    void parse_CurrentLineIsEmpty_ResultNull() {
        // Arrange
        Iterator<String> iterator = mockIterator("");
        String currentLine = "";
        TypeThreeParcelParser parser = new TypeThreeParcelParser();

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
        TypeThreeParcelParser parser = new TypeThreeParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertNull(result);
    }

    @Test
    void parse_CurrentLineIsAnotherNumber_ResultNull() {
        // Arrange
        Iterator<String> iterator = mockIterator("222");
        String currentLine = "222";
        TypeThreeParcelParser parser = new TypeThreeParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertNull(result);
    }

    @Test
    void parse_CurrentLineIsThreeAndOtherLinesExist_ResultCorrectParcel() {
        // Arrange
        Iterator<String> iterator = mockIterator("333", "other line");
        String currentLine = "333";
        TypeThreeParcelParser parser = new TypeThreeParcelParser();

        // Act
        Parcel result = parser.parse(iterator, currentLine);

        // Assert
        assertEquals(3, result.getType());
        assertEquals(3, result.getWidth());
        assertEquals(3, result.getTopWidth());
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