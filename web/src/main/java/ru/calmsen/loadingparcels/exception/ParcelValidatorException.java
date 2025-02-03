package ru.calmsen.loadingparcels.exception;

/**
 * Бизнес исключение, бросающиеся в случае, если посылка(и) не валидна.
 */
public class ParcelValidatorException extends BusinessException {
    public ParcelValidatorException(String message) {
        super(message);
    }
}
