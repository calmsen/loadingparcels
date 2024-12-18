package calmsen.command;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class Command {
    public abstract boolean isMatch(String command);
    public abstract void execute(CommandContext context);
}
