package ru.calmsen.loadingparcels.service.parser;

import ru.calmsen.loadingparcels.model.domain.Parcel;

import java.util.List;

/**
 * Парсер списка посылок
 */
public interface ParcelsParser {
    List<Parcel> parseParcelsFromFile(String fileName);
}
