package calmsen.util.parcelsparser;

import calmsen.exception.ParcelParserException;
import calmsen.model.domain.Parcel;
import calmsen.model.domain.enums.ParcelDimensionsType;

import java.util.Iterator;

public class TypeEightParcelParser extends ParcelParser {
    @Override
    public Parcel parse(Iterator<String> lines, String currentLine) {
        if (currentLine.trim().equals("8888")) {
            if (!lines.hasNext()) {
                throw new ParcelParserException(String.format("Не удалось распарсить посылку типа 8, так как поток строк завершился. Ожидалось: %s", "8888"));
            }

            currentLine = lines.next();
            if (currentLine.trim().equals("8888")) {
                return Parcel
                        .builder()
                        .type(8)
                        .width(4)
                        .height(2)
                        .topWidth(4)
                        .dimensionsType(ParcelDimensionsType.Dimensions4X2)
                        .build();
            }

            throw new ParcelParserException(String.format("Не удалось распарсить посылку типа 8. Текущая строка: %s. Ожидалось: %s", currentLine, "8888"));
        }

        return null;
    }
}
