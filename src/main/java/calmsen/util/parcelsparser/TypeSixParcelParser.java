package calmsen.util.parcelsparser;

import calmsen.exception.ParcelParserException;
import calmsen.model.domain.Parcel;
import calmsen.model.domain.enums.ParcelDimensionsType;

import java.util.Iterator;

public class TypeSixParcelParser extends ParcelParser {
    @Override
    public Parcel parse(Iterator<String> lines, String currentLine) {
        if (currentLine.trim().equals("666")) {
            if (!lines.hasNext()) {
                throw new ParcelParserException(String.format("Не удалось распарсить посылку типа 6, так как поток строк завершился. Ожидалось: %s", "666"));
            }

            currentLine = lines.next();
            if (currentLine.trim().equals("666")) {
                return Parcel
                        .builder()
                        .type(6)
                        .width(3)
                        .height(2)
                        .topWidth(3)
                        .dimensionsType(ParcelDimensionsType.Dimensions3X2)
                        .build();
            }

            throw new ParcelParserException(String.format("Не удалось распарсить посылку типа 6. Текущая строка: %s. Ожидалось: %s", currentLine, "666"));
        }

        return null;
    }
}
