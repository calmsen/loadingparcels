package ru.calmsen.loadingparcels.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Абстрактный класс команды.
 *
 * @param <TContext> тип контекста, который передается в защищенный метод execute.
 */
public abstract class Command<TContext> {
    /**
     * Сопоставляет первое слово в командной строке к имени команды.
     *
     * @param command командная строка, содержащая название команды и аргументы.
     * @return true если удалось найти. Иначе false
     */
    public boolean isMatch(String command) {
        if (command == null) {
            return false;
        }

        command = command.trim();
        var spacePos = command.indexOf(' ');
        if (spacePos == -1) {
            return command.equals(getName());
        }

        return command.substring(0, spacePos).equals(getName());
    }

    /**
     * Выполняет команду.
     *
     * @param command командная строка, содержащая название команды и аргументы.
     */
    public void handle(String command) {
        var context = toContext(command);
        execute(context);
    }

    protected abstract String getName();

    protected abstract void execute(TContext context);

    protected TContext toContext(String command) {
        return null;
    }

    /**
     * Преобразует команду в словарь. Разделяет строку на пары key-value по разделителю --.
     * А затем разделяет key-value. Ключом является первое слово, остальное является значением.
     * Значение можно обрамлять двойными кавычками, но необязательно.
     *
     * @param command командная строка, содержащая название команды и аргументы.
     * @return словарь аргументов команды
     */
    protected Map<String, String> toMap(String command) {
        command = command.replaceAll("\\\\n", "\n");
        var commandParts = Arrays.stream(command.trim().split("--"))
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
