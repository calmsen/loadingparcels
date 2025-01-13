package ru.calmsen.loadingparcels.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.service.ParcelsService;

/**
 * Команда удаления посылки.
 */
@Slf4j
@RequiredArgsConstructor
public class DeleteParcelCommand extends Command<String> {
    private final ParcelsService parcelsService;

    @Override
    protected String getName() {
        return "delete";
    }

    @Override
    protected String execute(String parcelName) {
        log.info("Удаление посылки \"{}\"", parcelName);
        parcelsService.deleteParcel(parcelName);
        log.info("Посылка \"{}\" удалена", parcelName);
        return null;
    }

    @Override
    protected String toContext(String command) {
        return toMap(command).get(getName());
    }
}
