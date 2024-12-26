package ru.calmsen.loadingparcels.service.parser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.model.domain.Box;
import ru.calmsen.loadingparcels.util.FileReader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class TxtParcelsParser {
    private final FileReader fileReader;

    public List<Box> parseParcelsFromFile(String fileName) {
        var lines = fileReader.readAllLines(fileName)
                .stream()
                .iterator();
        List<Box> parcels = new ArrayList<>();
        var rows = new ArrayList<List<Character>>();
        while (lines.hasNext()) {
            var currentLine = lines.next();
            if (currentLine.isBlank()) {
                if (!rows.isEmpty()) {
                    parcels.add(new Box(rows));
                }

                rows = new ArrayList<>();
                continue;
            }

            rows.add(currentLine.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
        }

        if (!rows.isEmpty()) {
            parcels.add(new Box(rows));
        }


        return parcels;
    }
}
