package ru.calmsen.billing.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Провайдер команды.
 */
@Component
@RequiredArgsConstructor
public class CommandProvider {
    private final List<Command<?>> commands;

    /**
     * Находит первую команду, перебирая все зарегистрированные команды на совпадение с командной строкой.
     *
     * @param targetCommand командная строка
     * @return контейнер с командой или пустой контейнер
     */
    public Optional<Command<?>> findCommand(String targetCommand) {
        for (Command<?> command : commands) {
            if (command.isMatch(targetCommand)) {
                return Optional.of(command);
            }
        }
        return Optional.empty();
    }
}
