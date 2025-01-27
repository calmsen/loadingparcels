package ru.calmsen.loadingparcels.command.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.calmsen.loadingparcels.command.Command;
import ru.calmsen.loadingparcels.mapper.ParcelsMapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.dto.ParcelDto;
import ru.calmsen.loadingparcels.service.ParcelsService;

import java.util.Map;

/**
 * Команда добавления посылки.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CreateParcelCommand extends Command<ParcelDto> {
    private final ParcelsService parcelsService;
    private final ParcelsMapper contextMapper;

    @Override
    protected String getName() {
        return "create";
    }

    @Override
    public String execute(ParcelDto parcel) {
        log.info("Добавление посылки \n{}", parcel);
        parcelsService.addParcel(parcel);
        log.info("Посылка \"{}\" добавлена", parcel.getName());
        return null;
    }

    @Override
    protected ParcelDto toContext(Map<String, String> args) {
        return contextMapper.toParcelDto(args);
    }
}
