package ru.calmsen.loadingparcels.util;

import ru.calmsen.loadingparcels.exception.FileReaderException;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class FileReader {
    public List<String> readAllLines(String fileName) {
        try {
            return Files.readAllLines(getFile(fileName).toPath());
        } catch (Exception e) {
            throw new FileReaderException("Не удалось прочитать файл: " + fileName, e);
        }
    }

    public String readString(String fileName) {
        try {
            return Files.readString(getFile(fileName).toPath());
        } catch (Exception e) {
            throw new FileReaderException("Не удалось прочитать файл: " + fileName, e);
        }
    }

    private File getFile(String fileName) {
        var resource = getClass().getClassLoader().getResource(fileName);
        if (resource != null) {
            return new File(resource.getFile());
        }
        return new File(fileName);
    }
}
