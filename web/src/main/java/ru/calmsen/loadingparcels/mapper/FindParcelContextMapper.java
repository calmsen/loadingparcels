package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.calmsen.loadingparcels.command.CommandParameter;
import ru.calmsen.loadingparcels.command.impl.FindParcelCommand;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;

import java.util.Map;

/**
 * Маппер для контекста команды find.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class FindParcelContextMapper {
    @Mapping(target = "parcelName", source = CommandParameter.FindParcel.NAME)
    @Mapping(target = "viewFormat", source = CommandParameter.FindParcel.OUT_FORMAT, defaultValue = CommandParameter.FindParcel.OUT_FORMAT_DEFAULT_VALUE)
    @Mapping(target = "pageNumber", source = CommandParameter.FindParcel.PAGE_NUMBER, defaultValue = CommandParameter.FindParcel.PAGE_NUMBER_DEFAULT_VALUE)
    @Mapping(target = "pageSize", source = CommandParameter.FindParcel.PAGE_SIZE, defaultValue = CommandParameter.FindParcel.PAGE_SIZE_DEFAULT_VALUE)
    public abstract FindParcelCommand.Context toContext(Map<String, String> map);

    public ViewFormat toViewFormat(String value){
        return ViewFormat.fromString(value);
    }
}
