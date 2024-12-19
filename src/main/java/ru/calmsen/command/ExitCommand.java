package ru.calmsen.command;

public class ExitCommand implements Command {
    @Override
    public boolean isMatch(String command) {
        return command.equals("exit");
    }

    @Override
    public void execute(CommandContext context) {
        System.exit(0);
    }
}
