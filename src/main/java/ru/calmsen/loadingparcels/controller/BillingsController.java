package ru.calmsen.loadingparcels.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.calmsen.loadingparcels.command.CommandSender;
import ru.calmsen.loadingparcels.command.impl.BillingCommand;
import ru.calmsen.loadingparcels.model.domain.Billing;
import ru.calmsen.loadingparcels.service.BillingsService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с посылками
 */
@ShellComponent
@RequiredArgsConstructor
public class BillingsController {
    private final BillingCommand billingCommand;

    /**
     * Детали счета
     *
     * @param user идентификатор пользователя
     * @param from дата от (включительно)
     * @param to   дата до (включительно)
     * @return детали счета
     */
    @ShellMethod("Детали счета")
    public String billing(String user, String from, String to) {
        return billingCommand.execute(Map.of(
                "user", user,
                "from", from,
                "to", to
        ));
    }
}
