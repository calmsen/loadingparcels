package ru.calmsen.loadingparcels.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.calmsen.loadingparcels.command.CommandParameter;
import ru.calmsen.loadingparcels.command.impl.BillingCommand;

import java.util.Map;

/**
 * Контроллер для работы со счетами
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
            CommandParameter.USER, user,
            CommandParameter.Billing.FROM, from,
            CommandParameter.Billing.TO, to
        ));
    }
}
