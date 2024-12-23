package ru.calmsen.loadingparcels.service.parser;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.mapper.TrucksMapper;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.dto.TruckDto;
import ru.calmsen.loadingparcels.util.FileReader;
import ru.calmsen.loadingparcels.util.JsonUtil;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class JsonTrucksParser {
    private final FileReader fileReader;
    private final TrucksMapper trucksMapper;
    public List<Truck> parseTrucksFromFile(String fileName) {
        var json = fileReader.readString(fileName);
        var trucksDto = Arrays.stream(JsonUtil.fromJson(json, TruckDto[].class)).toList();
        return trucksMapper.toTruckDomain(trucksDto);
    }
}
