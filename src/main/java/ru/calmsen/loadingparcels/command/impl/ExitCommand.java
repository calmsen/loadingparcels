package ru.calmsen.loadingparcels.command.impl;

import ru.calmsen.loadingparcels.command.Command;

/**
 * Команда выхода из приложения.
 */
public class ExitCommand extends Command<Void> {
    @Override
    protected String getName() {
        return "exit";
    }

    @Override
    public String execute(Void unused) {
        System.exit(0);
        return null;
    }
}
