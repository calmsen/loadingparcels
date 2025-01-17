package ru.calmsen.loadingparcels.exception;

/**
 * Бизнес исключение. Все бизнес исключения являются обработанными ошибками,
 * поэтому при логировании данных исключений не нужно логировать stacktrace.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(String message) {
        super(message);
    }
}
