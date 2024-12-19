package ru.calmsen.util;

import java.util.List;

public class ConsoleLinesWriter {
    public void writeAllLines(List<String> lines) {
        for (String line : lines) {
            System.out.println(line);
        }
    }
}
