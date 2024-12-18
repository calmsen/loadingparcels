package calmsen.util;

import calmsen.exception.FileLinesReaderException;

import java.util.List;

public class ConsoleLinesWriter {
    public void writeAllLines(List<String> lines) throws FileLinesReaderException {
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
