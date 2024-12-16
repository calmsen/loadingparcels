package calmsen.util.parcelsparser;

import calmsen.model.domain.Parcel;
import calmsen.model.domain.enums.ParcelDimensionsType;

import java.util.Iterator;

public class TypeOneParcelParser extends ParcelParser {
    @Override
    public Parcel parse(Iterator<String> lines, String currentLine) {
        if (currentLine.trim().equals("1")) {
            return Parcel
                    .builder()
                    .type(1)
                    .width(1)
                    .height(1)
                    .topWidth(1)
                    .dimensionsType(ParcelDimensionsType.Dimensions1X1)
                    .build();
        }

        return null;
    }
}
