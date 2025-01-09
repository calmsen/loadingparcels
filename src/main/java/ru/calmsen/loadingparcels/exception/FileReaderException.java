package ru.calmsen.loadingparcels.exception;

/**
 * Бизнес исключение, бросающиеся в случае, если не удалось прочитать файл.
 */
public class FileReaderException extends BusinessException {
    public FileReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
