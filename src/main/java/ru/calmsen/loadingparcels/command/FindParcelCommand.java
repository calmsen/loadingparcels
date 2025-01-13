package ru.calmsen.loadingparcels.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.mapper.FindParcelContextMapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.enums.OutputType;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.service.ParcelsService;
import ru.calmsen.loadingparcels.util.FileWriter;
import ru.calmsen.loadingparcels.view.factory.ParcelsViewFactory;

import java.util.List;

/**
 * Команда поиска посылок.
 */
@Slf4j
@RequiredArgsConstructor
public class FindParcelCommand extends Command<FindParcelCommand.Context> {
    private final ParcelsService parcelsService;
    private final ParcelsViewFactory parcelsViewFactory;
    private final FindParcelContextMapper contextMapper;

    @Override
    protected String getName() {
        return "find";
    }

    @Override
    protected String execute(Context context) {
        var logMessage = context.parcelName != null
                ? "Просмотр посылки " + context.parcelName
                : "Просмотр посылок";
        log.info(logMessage);

        var parcels = context.parcelName != null
                ? List.of(parcelsService.findParcel(context.parcelName))
                : parcelsService.findAllParcels();
        return parcelsViewFactory.createView(context.viewFormat).getOutputData(parcels);
    }

    @Override
    protected Context toContext(String command) {
        return contextMapper.toContext(toMap(command));
    }

    @Getter
    @Setter
    public static class Context {
        private String parcelName;
        private ViewFormat viewFormat;
    }
}
