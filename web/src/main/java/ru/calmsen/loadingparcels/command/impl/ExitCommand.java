package ru.calmsen.loadingparcels.command.impl;

import org.springframework.stereotype.Component;
import ru.calmsen.loadingparcels.command.Command;

/**
 * Команда выхода из приложения.
 */
@Component
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
