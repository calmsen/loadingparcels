package ru.calmsen.loadingparcels.service.parser.impl;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.mapper.TrucksMapper;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.dto.TruckDto;
import ru.calmsen.loadingparcels.service.parser.TrucksParser;
import ru.calmsen.loadingparcels.util.FileReader;
import ru.calmsen.loadingparcels.util.JsonUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Парсит из json список машин.
 */
@RequiredArgsConstructor
public class JsonTrucksParser implements TrucksParser {
    private final FileReader fileReader;
    private final TrucksMapper trucksMapper;

    /**
     * Парсит из json список машин.
     *
     * @param fileName наименование файла
     * @return список машин
     */
    @Override
    public List<Truck> parseTrucksFromFile(String fileName) {
        var json = fileReader.readString(fileName);
        var trucksDto = Arrays.stream(JsonUtil.fromJson(json, TruckDto[].class)).toList();
        return trucksMapper.toTruckDomain(trucksDto);
    }
}
