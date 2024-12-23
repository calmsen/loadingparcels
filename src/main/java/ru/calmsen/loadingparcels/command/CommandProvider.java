package ru.calmsen.loadingparcels.command;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class CommandProvider {
    private final List<Command> commands;

    public Optional<Command> findCommand(String targetCommand) {
        for (Command command : commands) {
            if (command.isMatch(targetCommand)) {
                return Optional.of(command);
            }
        }
        return Optional.empty();
    }
}
