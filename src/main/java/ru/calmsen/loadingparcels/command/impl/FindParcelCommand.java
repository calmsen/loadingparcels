package ru.calmsen.loadingparcels.command.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.command.Command;
import ru.calmsen.loadingparcels.mapper.FindParcelContextMapper;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.view.ParcelsView;

import java.util.List;
import java.util.Map;

/**
 * Команда поиска посылок.
 */
@Slf4j
@RequiredArgsConstructor
public class FindParcelCommand extends Command<FindParcelCommand.Context> {
    private final ParcelsService parcelsService;
    private final Map<ViewFormat, ParcelsView> parcelsViews;
    private final FindParcelContextMapper contextMapper;

    @Override
    protected String getName() {
        return "find";
    }

    @Override
    public String execute(Context context) {
        var logMessage = context.parcelName != null
                ? "Просмотр посылки " + context.parcelName
                : "Просмотр посылок";
        log.info(logMessage);

        var parcels = context.parcelName != null
                ? List.of(parcelsService.findParcel(context.parcelName))
                : parcelsService.findAllParcels();
        return parcelsViews.get(context.viewFormat).buildOutputData(parcels);
    }

    @Override
    protected Context toContext(Map<String, String> args) {
        return contextMapper.toContext(args);
    }

    @Getter
    @Setter
    public static class Context {
        private String parcelName;
        private ViewFormat viewFormat;
    }
}
