package calmsen.util.parcelsparser;

import calmsen.exception.ParcelParserException;
import calmsen.model.domain.Parcel;
import calmsen.model.domain.enums.ParcelDimensionsType;

import java.util.Iterator;

public class TypeSevenParcelParser extends ParcelParser {
    @Override
    public Parcel parse(Iterator<String> lines, String currentLine) {
        if (currentLine.trim().equals("777")) {
            if (!lines.hasNext()) {
                throw new ParcelParserException(String.format("Не удалось распарсить посылку типа 7, так как поток строк завершился. Ожидалось: %s", "7777"));
            }

            currentLine = lines.next();
            if (currentLine.trim().equals("7777")) {
                return Parcel
                        .builder()
                        .type(7)
                        .width(4)
                        .height(2)
                        .topWidth(3)
                        .dimensionsType(ParcelDimensionsType.Dimensions4X2)
                        .build();
            }

            throw new ParcelParserException(String.format("Не удалось распарсить посылку типа 7. Текущая строка: %s. Ожидалось: %s", currentLine, "7777"));
        }

        return null;
    }
}
