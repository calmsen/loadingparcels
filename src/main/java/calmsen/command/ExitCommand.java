package calmsen.command;

public class ExitCommand extends Command{
    @Override
    public boolean isMatch(String command) {
        return command.equals("exit");
    }

    @Override
    public void execute(CommandContext context) {
        System.exit(0);
    }
}
