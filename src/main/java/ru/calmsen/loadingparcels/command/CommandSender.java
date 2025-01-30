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
    public static final String DEFAULT_COMMAND = "load input.csv";
    private final CommandProvider commandProvider;

    /**
     * Выполняет команды
     *
     * @param command командная строка, содержащая название команды и аргументы.
     * @return объект с результатом. Содержит данные или ошибку.
     */
    public String send(String command) {
        if (command == null || command.isBlank()) {
            command = DEFAULT_COMMAND;
        }

        var commandName = command.split(" ")[0];
        var args = toMap(command);
        return send(commandName, args);
    }

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

    /**
     * Преобразует аргументы в словарь. Разделяет строку на пары key-value по разделителю --.
     * А затем разделяет key-value. Ключом является первое слово, остальное является значением.
     * Значение можно обрамлять двойными кавычками, но необязательно.
     *
     * @param args аргументы.
     * @return словарь аргументов команды
     */
    private Map<String, String> toMap(String args) {
        args = args.replaceAll("\\\\n", "\n");
        var commandParts = Arrays.stream(args.trim().split("--"))
                .filter(s -> !s.isEmpty())
                .toList();
        Map<String, String> map = new HashMap<>();
        for (var commandPart : commandParts) {
            if (commandPart.indexOf(' ') == -1) {
                continue;
            }

            var key = commandPart.substring(0, commandPart.indexOf(' '));
            var value = commandPart.substring(commandPart.indexOf(' ') + 1).trim();
            value = stripQuotes(value);
            if (key.isEmpty() || value.isEmpty()) {
                continue;
            }

            map.put(key, value);
        }

        return map;
    }

    private String stripQuotes(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }

        return value;
    }
}
