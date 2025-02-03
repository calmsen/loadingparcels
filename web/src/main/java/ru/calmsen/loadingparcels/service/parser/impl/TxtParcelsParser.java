package ru.calmsen.loadingparcels.service.parser.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.service.parser.ParcelsParser;
import ru.calmsen.loadingparcels.util.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Парсит из текстового файла список посылок.
 */
@Slf4j
@RequiredArgsConstructor
public class TxtParcelsParser implements ParcelsParser {
    private final FileReader fileReader;

    /**
     * Парсит из текстового файла список посылок.
     *
     * @param fileName наименование файла.
     * @return список посылок
     */
    @Override
    public List<Parcel> parseParcelsFromFile(String fileName) {
        var lines = fileReader.readAllLines(fileName)
                .stream()
                .iterator();
        List<Parcel> parcels = new ArrayList<>();
        var rows = new ArrayList<List<Character>>();
        while (lines.hasNext()) {
            var currentLine = lines.next();
            if (currentLine.isBlank()) {
                if (!rows.isEmpty()) {
                    parcels.add(new Parcel(rows));
                }

                rows = new ArrayList<>();
                continue;
            }

            rows.add(currentLine.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
        }

        if (!rows.isEmpty()) {
            parcels.add(new Parcel(rows));
        }


        return parcels;
    }
}
