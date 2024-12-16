package calmsen.util.parcelsparser;

import calmsen.model.domain.Parcel;
import calmsen.model.domain.enums.ParcelDimensionsType;

import java.util.Iterator;

public class TypeFourParcelParser extends ParcelParser {
    @Override
    public Parcel parse(Iterator<String> lines, String currentLine) {
        if (currentLine.trim().equals("4444")) {
            return Parcel
                    .builder()
                    .type(4)
                    .width(4)
                    .height(1)
                    .topWidth(4)
                    .dimensionsType(ParcelDimensionsType.Dimensions4X1)
                    .build();
        }

        return null;
    }
}
