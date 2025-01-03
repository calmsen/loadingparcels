package ru.calmsen.loadingparcels.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.command.CommandContext;
import ru.calmsen.loadingparcels.command.CommandProvider;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.util.ConsoleOutputDataWriter;

import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public class ParcelsController {
    private final CommandProvider commandProvider;
    private final ConsoleOutputDataWriter consoleWriter;

    public void listen() {
        var scanner = new Scanner(System.in);

        var DEFAULT_COMMAND = "load input.txt";
        consoleWriter.write("Введите команду / по умолчанию " + DEFAULT_COMMAND);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.isEmpty()) {
                command = DEFAULT_COMMAND;
            }

            var foundCommand = commandProvider.findCommand(command);
            if (foundCommand.isPresent()) {
                try {
                    foundCommand.get().execute(new CommandContext(command));
                } catch (BusinessException e) {
                    consoleWriter.write(e.getMessage());
                } catch (Exception e) {
                    log.error("При выполнении команды {} произошла ошибка", command, e);
                }
            }

            consoleWriter.write("Введите команду / по умолчанию " + DEFAULT_COMMAND);
        }
    }
}
