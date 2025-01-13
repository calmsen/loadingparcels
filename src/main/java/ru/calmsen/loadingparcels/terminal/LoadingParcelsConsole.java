package ru.calmsen.loadingparcels.terminal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.controller.ParcelsController;

import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
public class LoadingParcelsConsole {
    private final ParcelsController parcelsController;

    /**
     * Слушает команды из командной строки.
     */
    public void listen() {
        var scanner = new Scanner(System.in);
        write("Введите команду / по умолчанию " + ParcelsController.DEFAULT_COMMAND);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            var result = parcelsController.handleCommand(command);

            if (result.data() != null){
                write(result.data());
            }

            if (result.error() != null){
                write(result.error());
            }

            write("Введите команду / по умолчанию " + ParcelsController.DEFAULT_COMMAND);
        }
    }

    private void write(String output){
        System.out.println(output);
    }
}
