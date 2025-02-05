package ru.calmsen.billing.command;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Абстрактный класс команды.
 *
 * @param <T> тип контекста, который передается в метод execute.
 */
public abstract class Command<T> {

    /**
     * Сопоставляет первое слово в командной строке к имени команды.
     *
     * @param commandName название команды.
     * @return true если удалось найти. Иначе false
     */
    public boolean isMatch(String commandName) {
        if (commandName == null) {
            return false;
        }

        return commandName.equals(getName());
    }

    /**
     * Выполняет команду.
     *
     * @param args аргументы.
     */
    public String execute(Map<String, String> args) {
        var context = toContext(removeEmptyEntries(args));
        return execute(context);
    }

    public abstract String execute(T context);

    /**
     * Преобразовать в контекст
     *
     * @param args словарь с аргументами
     * @return контекст команды
     */
    protected T toContext(Map<String, String> args) {
        return null;
    }

    protected abstract String getName();

    private Map<String, String> removeEmptyEntries(Map<String, String> map) {
        return map.entrySet().stream()
                .filter(x -> x.getValue() != null && !x.getValue().isBlank())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
