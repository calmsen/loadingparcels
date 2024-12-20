package ru.calmsen.loadingparcels.command;

public interface Command {
    boolean isMatch(String command);

    void execute(CommandContext context);
}
