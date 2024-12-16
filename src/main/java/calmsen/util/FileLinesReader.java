package calmsen.util;

import calmsen.exception.FileLinesReaderException;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class FileLinesReader {
    public List<String> readAllLines(String filePath) {
        try {
            return Files.readAllLines(new File(getClass().getClassLoader().getResource(filePath).toURI()).toPath());
        } catch (Exception e) {
            throw new FileLinesReaderException("Не удалось прочитать файл: " + filePath, e);
        }
    }
}
