package ru.calmsen.loadingparcels.validator;

import org.junit.jupiter.api.Test;
import ru.calmsen.loadingparcels.model.domain.Parcel;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ParcelValidatorTest {

    @Test
    void validate_ParcelIsNull_ReturnsError() {
        // Arrange
        ParcelValidator validator = new ParcelValidator();
        Parcel parcel = null;

        // Act
        List<String> errors = validator.validate(parcel);

        // Assert
        assertThat(errors)
                .hasSize(1)
                .contains("Посылка пустая: null");
    }

    @Test
    void validate_ParcelHasNoContent_ReturnsError() {
        // Arrange
        ParcelValidator validator = new ParcelValidator();
        Parcel parcel = new Parcel(Collections.emptyList());

        // Act
        List<String> errors = validator.validate(parcel);

        // Assert
        assertThat(errors)
            .hasSize(1)
            .contains("Посылка пустая: " + parcel);
    }

    @Test
    void validate_ParcelContainsNonDigitSymbols_ReturnsError() {
        // Arrange
        ParcelValidator validator = new ParcelValidator();
        Parcel parcel = new Parcel(List.of(
                List.of('A', 'B', 'C')
        ));

        // Act
        List<String> errors = validator.validate(parcel);

        // Assert
        assertThat(errors)
            .hasSize(3)
            .contains("В посылке найден символ не являющиеся цифрой: " + parcel)
            .contains("Все цифры в посылке должны быть одинаковые: " + parcel)
            .contains("Количество цифр в посылке должно быть равно первой цифре в посылке: " + parcel);
    }

    @Test
    void validate_AllDigitsAreNotTheSame_ReturnsError() {
        // Arrange
        ParcelValidator validator = new ParcelValidator();
        Parcel parcel = new Parcel(List.of(
                List.of('1', '2', '3')
        ));

        // Act
        List<String> errors = validator.validate(parcel);

        // Assert
        assertThat(errors)
            .hasSize(2)
            .contains("Все цифры в посылке должны быть одинаковые: " + parcel)
            .contains("Количество цифр в посылке должно быть равно первой цифре в посылке: " + parcel);
    }

    @Test
    void validate_DimensionsDoNotEqualFirstDigit_ReturnsError() {
        // Arrange
        ParcelValidator validator = new ParcelValidator();
        Parcel parcel = new Parcel(List.of(
                List.of('2', '2', '2')
        ));

        // Act
        List<String> errors = validator.validate(parcel);

        // Assert
        assertThat(errors)
            .hasSize(1)
            .contains("Количество цифр в посылке должно быть равно первой цифре в посылке: " + parcel);
    }

    @Test
    void validate_AllValidationsPass_NoErrorsReturned() {
        // Arrange
        ParcelValidator validator = new ParcelValidator();
        Parcel parcel = new Parcel(List.of(
                List.of('3', '3', '3')
        ));

        // Act
        List<String> errors = validator.validate(parcel);

        // Assert
        assertThat(errors).isEmpty();
    }

    @Test
    void validate_ComplexCase_MultipleErrors() {
        // Arrange
        ParcelValidator validator = new ParcelValidator();
        Parcel parcel = new Parcel(List.of(
                List.of('1', 'A', '1'),
                List.of('1', '1', '1')
        ));

        // Act
        List<String> errors = validator.validate(parcel);

        // Assert
        assertThat(errors)
            .hasSize(3)
            .contains("В посылке найден символ не являющиеся цифрой: " + parcel)
            .contains("Все цифры в посылке должны быть одинаковые: " + parcel)
            .contains("Количество цифр в посылке должно быть равно первой цифре в посылке: " + parcel);
    }
}