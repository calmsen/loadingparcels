package ru.calmsen.loadingparcels.command.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.command.Command;
import ru.calmsen.loadingparcels.mapper.ParcelsMapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.service.ParcelsService;

import java.util.Map;

/**
 * Команда добавления посылки.
 */
@Slf4j
@RequiredArgsConstructor
public class CreateParcelCommand extends Command<Parcel> {
    private final ParcelsService parcelsService;
    private final ParcelsMapper contextMapper;

    @Override
    protected String getName() {
        return "create";
    }

    @Override
    public String execute(Parcel parcel) {
        log.info("Добавление посылки \n{}", parcel);
        parcelsService.addParcel(parcel);
        log.info("Посылка \"{}\" добавлена", parcel.getName());
        return null;
    }

    @Override
    protected Parcel toContext(Map<String, String> args) {
        return contextMapper.toParcel(args);
    }
}
