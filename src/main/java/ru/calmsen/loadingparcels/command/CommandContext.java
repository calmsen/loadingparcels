package ru.calmsen.loadingparcels.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Scanner;

public class CommandContext {
    @Getter
    private final String command;
    private final String[] args;

    public CommandContext(String command) {
        this.command = command;
        this.args = Arrays.stream(command.split(" ")).filter(s -> !s.isEmpty()).toArray(String[]::new);
    }

    public String getArgValue(String name, String defaultValue) {
        for (int i = 0, j = 1; i < args.length; i++, j++) {
            if (args[i].equals(name) && j < args.length && !args[j].startsWith("--")) {
                return args[j];
            }
        }

        return defaultValue;
    }

    public String getArgValueIfFound(String name, String defaultValue) {
        for (int i = 0, j = 1; i < args.length; i++, j++) {
            if (args[i].equals(name)) {
                return j < args.length && !args[j].startsWith("--") ? args[j] : defaultValue;
            }
        }

        return "";
    }
}
