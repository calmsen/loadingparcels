package calmsen.util.parcelsparser;

import calmsen.model.domain.Parcel;
import calmsen.model.domain.enums.ParcelDimensionsType;

import java.util.Iterator;

public class TypeFiveParcelParser extends ParcelParser {
    @Override
    public Parcel parse(Iterator<String> lines, String currentLine) {
        if (currentLine.trim().equals("55555")) {
            return Parcel
                    .builder()
                    .type(5)
                    .width(5)
                    .height(1)
                    .topWidth(5)
                    .dimensionsType(ParcelDimensionsType.Dimensions5X1)
                    .build();
        }

        return null;
    }
}
