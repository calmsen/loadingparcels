package ru.calmsen.loadingparcels.command.impl;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
@Component
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
                : parcelsService.findAllParcels(context.pageNumber, context.pageSize);
        return parcelsViews.get(context.viewFormat).buildOutputData(parcels);
    }

    @Override
    protected Context toContext(Map<String, String> args) {
        return contextMapper.toContext(args);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Context {
        private String parcelName;
        private ViewFormat viewFormat;
        private int pageNumber;
        private int pageSize;
    }
}
