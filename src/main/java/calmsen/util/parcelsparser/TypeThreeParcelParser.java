package calmsen.util.parcelsparser;

import calmsen.model.domain.Parcel;
import calmsen.model.domain.enums.ParcelDimensionsType;

import java.util.Iterator;

public class TypeThreeParcelParser extends ParcelParser {
    @Override
    public Parcel parse(Iterator<String> lines, String currentLine) {
        if (currentLine.trim().equals("333")) {
            return Parcel
                    .builder()
                    .type(3)
                    .width(3)
                    .height(1)
                    .topWidth(3)
                    .dimensionsType(ParcelDimensionsType.Dimensions3X1)
                    .build();
        }

        return null;
    }
}
