package ru.calmsen.loadingparcels.command;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommandProvider {
    private final List<Command> commands;

    public Command findCommand(String targetCommand) {
        for (Command command : commands) {
            if (command.isMatch(targetCommand)) {
                return command;
            }
        }
        return null;
    }
}
