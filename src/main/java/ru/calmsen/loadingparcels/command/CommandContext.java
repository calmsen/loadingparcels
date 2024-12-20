package ru.calmsen.loadingparcels.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Scanner;

@Getter
@RequiredArgsConstructor
public class CommandContext {
    private final String command;
    private final Scanner scanner;
    @Setter
    private String error;

}
