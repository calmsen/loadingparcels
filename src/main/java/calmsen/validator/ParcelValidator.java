package calmsen.validator;

import calmsen.model.domain.Parcel;
import calmsen.model.domain.enums.ParcelDimensionsType;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ParcelValidator {
    public boolean validate(Parcel parcel) {
        if (parcel == null || parcel.getContent().isEmpty() || parcel.getContent().stream().anyMatch(List::isEmpty)) {
            return false;
        }

        var firstSymbol = parcel.getContent().getFirst().getFirst();
        var firstSymbolIsDigit = firstSymbol >= '1' && firstSymbol <= '9';
        var isAllSame = parcel.getContent().stream()
                .flatMap(Collection::stream)
                .allMatch(x -> x == firstSymbol);

        return firstSymbolIsDigit && isAllSame && isValidDimensions(parcel);
    }

    private boolean isValidDimensions(Parcel parcel) {
        var firstSymbol = parcel.getContent().getFirst().getFirst();
        var isValidDimensions = parcel.getDimensions() == (firstSymbol - '0');
        if (!isValidDimensions) {
            return false;
        }

        // дальше сравним по строкам и колонкам
        if (firstSymbol < '6') {
            return parcel.getHeight() == 1
                    && parcel.getContent().getFirst().size() == (firstSymbol - '0');
        }

        if (firstSymbol == '6') {
            return parcel.getHeight() == 2
                    && parcel.getWidth(0) == 3
                    && parcel.getWidth(1) == 3;
        }

        if (firstSymbol == '7') {
            return parcel.getContent().size() == 2
                    && parcel.getWidth(0) == 3
                    && parcel.getWidth(1) == 4;
        }

        if (firstSymbol == '8') {
            return parcel.getContent().size() == 2
                    && parcel.getWidth(0) == 4
                    && parcel.getWidth(1) == 4;
        }

        if (firstSymbol == '9') {
            return parcel.getContent().size() == 3
                    && parcel.getWidth(0) == 3
                    && parcel.getWidth(1) == 3
                    && parcel.getWidth(2) == 3;
        }

        return false;
    }
}
