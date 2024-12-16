package calmsen.util.parcelsparser;

import calmsen.exception.ParcelParserException;
import calmsen.model.domain.Parcel;
import calmsen.model.domain.enums.ParcelDimensionsType;

import java.util.Iterator;

public class TypeNineParcelParser extends ParcelParser {
    @Override
    public Parcel parse(Iterator<String> lines, String currentLine) {
        if (currentLine.trim().equals("999")) {
            if (!lines.hasNext()) {
                throw new ParcelParserException(String.format("Не удалось распарсить посылку типа 9, так как поток строк завершился. Ожидалось: %s", "999"));
            }
            currentLine = lines.next();
            if (!currentLine.trim().equals("999")) {
                throw new ParcelParserException(String.format("Не удалось распарсить посылку типа 9. Текущая строка: %s. Ожидалось: %s", currentLine, "999"));

            }

            if (!lines.hasNext()) {
                throw new ParcelParserException(String.format("Не удалось распарсить посылку типа 9, так как поток строк завершился. Ожидалось: %s", "999"));
            }
            currentLine = lines.next();
            if (!currentLine.trim().equals("999")) {
                throw new ParcelParserException(String.format("Не удалось распарсить посылку типа 9. Текущая строка: %s. Ожидалось: %s", currentLine, "999"));

            }

            return Parcel
                    .builder()
                    .type(9)
                    .width(3)
                    .height(3)
                    .topWidth(3)
                    .dimensionsType(ParcelDimensionsType.Dimensions3X3)
                    .build();
        }

        return null;
    }
}
