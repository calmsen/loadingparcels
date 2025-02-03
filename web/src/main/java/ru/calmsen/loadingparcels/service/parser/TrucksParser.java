package ru.calmsen.loadingparcels.service.parser;

import ru.calmsen.loadingparcels.model.domain.Truck;

import java.util.List;

/**
 * Парсер списка машин
 */
public interface TrucksParser {
    List<Truck> parseTrucksFromFile(String fileName);
}
