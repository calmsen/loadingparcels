package ru.calmsen.loadingparcels.command.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.command.Command;
import ru.calmsen.loadingparcels.mapper.ParcelsMapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.service.ParcelsService;

import java.util.Map;

/**
 * Команда редактирования посылки.
 */
@Slf4j
@RequiredArgsConstructor
public class UpdateParcelCommand extends Command<Parcel> {
    private final ParcelsService parcelsService;
    private final ParcelsMapper contextMapper;

    @Override
    protected String getName() {
        return "update";
    }

    @Override
    public String execute(Parcel parcel) {
        log.info("Изменение посылки \"{}\"", parcel.getName());
        parcelsService.updateParcel(parcel);
        log.info("Посылка изменена \n{}", parcel);
        return null;
    }

    @Override
    protected Parcel toContext(Map<String, String> args) {
        return contextMapper.toParcel(args);
    }
}
