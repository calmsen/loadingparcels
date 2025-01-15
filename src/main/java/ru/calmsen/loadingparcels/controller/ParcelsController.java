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

        var finalCommand = command;
        return commandProvider.findCommand(finalCommand)
                .map(x -> {
                    try {
                        var result = x.handle(finalCommand);
                        return new ResultWrapper(result, null);
                    } catch (BusinessException e) {
                        return new ResultWrapper(null, e.getMessage());
                    } catch (Exception e) {
                        var errorMessage = String.format("При выполнении команды %s произошла ошибка", finalCommand);
                        log.error(errorMessage, e);
                        return new ResultWrapper(null, errorMessage);
                    }
                })
                .orElse(new ResultWrapper(null, String.format("Такой команды нет: %s", finalCommand)));
    }
}
