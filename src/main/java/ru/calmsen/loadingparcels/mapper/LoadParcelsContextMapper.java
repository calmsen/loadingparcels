package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.calmsen.loadingparcels.command.constant.CommandParameter;
import ru.calmsen.loadingparcels.command.impl.LoadParcelsCommand;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Маппер для контекста команды load.
 */
@Mapper
public abstract class LoadParcelsContextMapper {
    public static final int TRUCK_DEFAULT_WIDTH = 6;
    public static final int TRUCK_DEFAULT_HEIGHT = 6;
    public static final int TRUCKS_COUNT = 50;

    @Mapping(target = "inFile", source = CommandParameter.LoadParcels.IN_FILE)
    @Mapping(target = "parcelNames", source = CommandParameter.LoadParcels.PARCEL_NAMES)
    @Mapping(target = "outFile", source = CommandParameter.LoadParcels.OUT_FILE)
    @Mapping(target = "viewFormat", source = CommandParameter.LoadParcels.OUT_FORMAT,
            defaultValue = CommandParameter.LoadParcels.OUT_FORMAT_DEFAULT_VALUE)
    @Mapping(target = "loadingMode", source = CommandParameter.LoadParcels.LOADING_MODE,
            defaultValue = CommandParameter.LoadParcels.LOADING_MODE_DEFAULT_VALUE)
    public abstract LoadParcelsCommand.Context toContext(Map<String, String> map);

    @AfterMapping
    public LoadParcelsCommand.Context toContextAfterMapping(Map<String, String> map, @MappingTarget LoadParcelsCommand.Context context) {
        fillTrucksIfEmpty(map, context);
        return context;
    }

    public List<Truck> toTrucks(String value) {
        List<Truck> trucks = new ArrayList<>();
        for (var truckSize : value.split(Pattern.quote("\n"))) {
            var truckSizeParts = truckSize.split("x");
            var width = Integer.parseInt(truckSizeParts[0]);
            var height = Integer.parseInt(truckSizeParts[1]);
            trucks.add(new Truck(width, height));
        }
        return trucks;
    }

    public List<String> toParcelNames(String value) {
        return Arrays.stream(value.split(Pattern.quote("\n"))).toList();
    }

    public LoadingMode toLoadingMode(String value) {
        return LoadingMode.fromString(value);
    }

    public ViewFormat toViewFormat(String value) {
        return ViewFormat.fromString(value);
    }

    private void fillTrucksIfEmpty(Map<String, String> map, LoadParcelsCommand.Context context) {
        if (context.getTrucks() != null && !context.getTrucks().isEmpty()) {
            return;
        }

        var trucksCount = map.containsKey("trucks-count")
                ? Integer.parseInt(map.get("trucks-count"))
                : TRUCKS_COUNT;

        var trucksWidth = map.containsKey("trucks-width")
                ? Integer.parseInt(map.get("trucks-width"))
                : TRUCK_DEFAULT_WIDTH;

        var trucksHeight = map.containsKey("trucks-height")
                ? Integer.parseInt(map.get("trucks-height"))
                : TRUCK_DEFAULT_HEIGHT;
        List<Truck> trucks = new ArrayList<>();
        for (int i = 0; i < trucksCount; i++) {
            trucks.add(new Truck(trucksWidth, trucksHeight));
        }

        context.setTrucks(trucks);
    }
}
