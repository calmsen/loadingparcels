package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.calmsen.loadingparcels.command.FindParcelCommand;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;

import java.util.Map;

/**
 * Маппер для контекста команды find.
 */
@Mapper
public abstract class FindParcelContextMapper {
    @Mapping(target = "parcelName", source = "find")
    @Mapping(target = "viewFormat", source = "out-format", defaultValue = "TXT")
    public abstract FindParcelCommand.Context toContext(Map<String, String> map);

    public ViewFormat toViewFormat(String value){
        return ViewFormat.fromString(value);
    }
}
