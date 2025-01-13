package ru.calmsen.loadingparcels.command;

/**
 * Команда выхода из приложения.
 */
public class ExitCommand extends Command<Void> {
    @Override
    protected String getName() {
        return "exit";
    }

    @Override
    protected String execute(Void unused) {
        System.exit(0);
        return null;
    }
}
