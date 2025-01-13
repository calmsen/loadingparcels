package ru.calmsen.loadingparcels.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.command.CommandProvider;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.model.dto.ResultWrapper;

/**
 * Класс контроллера посылок.
 */
@Slf4j
@RequiredArgsConstructor
public class ParcelsController {
    public static final String DEFAULT_COMMAND = "load input.csv";
    private final CommandProvider commandProvider;

    /**
     * Выполняет команды
     *
     * @param command командная строка, содержащая название команды и аргументы.
     * @return объект с результатом. Содержит данные или ошибку.
     */
    public ResultWrapper handleCommand(String command) {
        if (command.isEmpty()) {
            command = DEFAULT_COMMAND;
        }

        var foundCommand = commandProvider.findCommand(command);
        if (foundCommand.isPresent()) {
            try {
                var result = foundCommand.get().handle(command);
                return new ResultWrapper(result, null);
            } catch (BusinessException e) {
                return new ResultWrapper(null, e.getMessage());
            } catch (Exception e) {
                log.error("При выполнении команды {} произошла ошибка", command, e);
            }
        }

        return new ResultWrapper(null, null);
    }
}
