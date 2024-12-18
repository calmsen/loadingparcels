package calmsen.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Scanner;

@Getter
@RequiredArgsConstructor
public class CommandContext {
    private final String command;
    private final Scanner scanner;
}
