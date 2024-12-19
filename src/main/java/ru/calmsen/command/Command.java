package ru.calmsen.command;

public interface Command {
    boolean isMatch(String command);

    void execute(CommandContext context);
}
