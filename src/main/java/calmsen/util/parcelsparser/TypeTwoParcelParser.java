package calmsen.util.parcelsparser;

import calmsen.model.domain.Parcel;
import calmsen.model.domain.enums.ParcelDimensionsType;

import java.util.Iterator;

public class TypeTwoParcelParser extends ParcelParser {
    @Override
    public Parcel parse(Iterator<String> lines, String currentLine) {
        if (currentLine.trim().equals("22")) {
            return Parcel
                    .builder()
                    .type(2)
                    .width(2)
                    .height(1)
                    .topWidth(2)
                    .dimensionsType(ParcelDimensionsType.Dimensions2X1)
                    .build();
        }

        return null;
    }
}
