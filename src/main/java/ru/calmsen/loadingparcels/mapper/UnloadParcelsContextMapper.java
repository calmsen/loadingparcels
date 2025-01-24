package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.calmsen.loadingparcels.command.impl.UnloadParcelsCommand;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;

import java.util.Map;

/**
 * Маппер для контекста команды unload.
 */
@Mapper
public abstract class UnloadParcelsContextMapper {
    @Mapping(target = "inFile", source = "unload")
    @Mapping(target = "outFile", source = "out-file")
    @Mapping(target = "viewFormat", source = "out-format", defaultValue = "TXT")
    @Mapping(target = "withCount", source = "with-count")
    public abstract UnloadParcelsCommand.Context toContext(Map<String, String> map);

    public ViewFormat toViewFormat(String value){
        return ViewFormat.fromString(value);
    }
}
