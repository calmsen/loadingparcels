package ru.calmsen.loadingparcels.command.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.calmsen.loadingparcels.command.Command;
import ru.calmsen.loadingparcels.service.ParcelsService;

import java.util.Map;

/**
 * Команда удаления посылки.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteParcelCommand extends Command<String> {
    private final ParcelsService parcelsService;

    @Override
    protected String getName() {
        return "delete";
    }

    @Override
    public String execute(String parcelName) {
        log.info("Удаление посылки \"{}\"", parcelName);
        parcelsService.deleteParcel(parcelName);
        log.info("Посылка \"{}\" удалена", parcelName);
        return null;
    }

    @Override
    protected String toContext(Map<String, String> args) {
        return args.get(getName());
    }
}
