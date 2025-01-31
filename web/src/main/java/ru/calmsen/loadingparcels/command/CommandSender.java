package ru.calmsen.loadingparcels.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.calmsen.loadingparcels.exception.BusinessException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс обработчика команд.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommandSender {
    private final CommandProvider commandProvider;

    public String send(String commandName, Map<String, String> args) {
        return commandProvider.findCommand(commandName)
                .map(x -> {
                    try {
                        return x.execute(args);
                    } catch (BusinessException e) {
                        return e.getMessage();
                    } catch (Exception e) {
                        var errorMessage = String.format("При выполнении команды %s произошла ошибка", commandName);
                        log.error(errorMessage, e);
                        return errorMessage;
                    }
                })
                .orElse(String.format("Такой команды нет: %s", commandName));
    }
}
