package ru.calmsen.loadingparcels.validator;

import ru.calmsen.loadingparcels.model.domain.Box;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ParcelValidator {
    public List<String> validate(Box parcel) {
        var errors = new ArrayList<String>();

        if (isParcelEmpty(parcel)) {
            errors.add("Посылка пустая: " + parcel);
            return errors;
        }

        if (!isAllDigitSymbols(parcel)) {
            errors.add("В посылке найден символ не являющиеся цифрой: " + parcel);
        }

        if (!isAllDigitsTheSame(parcel)) {
            errors.add("Все цифры в посылке должны быть одинаковые: " + parcel);
        }

        if (!isDimensionsEqualsFirstDigit(parcel)) {
            errors.add("Количество цифр в посылке должно быть равно первой цифре в посылке: " + parcel);
        }

        return errors;
    }

    private static boolean isParcelEmpty(Box parcel) {
        return parcel == null || parcel.getContent().isEmpty() || parcel.getContent().stream().anyMatch(List::isEmpty);
    }

    private boolean isAllDigitSymbols(Box parcel) {
        return parcel.getContent().stream()
                .flatMap(Collection::stream)
                .allMatch(x -> x >= '1' && x <= '9');
    }

    private boolean isAllDigitsTheSame(Box parcel) {
        var firstSymbol = parcel.getContent().getFirst().getFirst();
        return parcel.getContent().stream()
                .flatMap(Collection::stream)
                .allMatch(x -> x == firstSymbol);
    }


    private boolean isDimensionsEqualsFirstDigit(Box parcel) {
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
