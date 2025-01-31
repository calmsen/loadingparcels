package ru.calmsen.loadingparcels.command.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.calmsen.loadingparcels.command.Command;
import ru.calmsen.loadingparcels.mapper.ParcelsMapper;
import ru.calmsen.loadingparcels.model.dto.ParcelDto;
import ru.calmsen.loadingparcels.service.ParcelsService;

import java.util.Map;

/**
 * Команда редактирования посылки.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateParcelCommand extends Command<ParcelDto> {
    private final ParcelsService parcelsService;
    private final ParcelsMapper contextMapper;

    @Override
    protected String getName() {
        return "update";
    }

    @Override
    public String execute(ParcelDto parcel) {
        log.info("Изменение посылки \"{}\"", parcel.getName());
        parcelsService.updateParcel(parcel);
        log.info("Посылка изменена \n{}", parcel);
        return null;
    }

    @Override
    protected ParcelDto toContext(Map<String, String> args) {
        return contextMapper.toParcelDto(args);
    }
}
