package ru.calmsen.loadingparcels.exception;

/**
 * Бизнес исключение, бросающиеся в случае, если не удалось записать файл.
 */
public class FileWriterException extends BusinessException {
    public FileWriterException(String message, Throwable cause) {
        super(message, cause);
    }
}
