package calmsen.util;

import calmsen.model.domain.Parcel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class ParcelsParser {
    private final FileLinesReader fileLinesReader;

    public List<Parcel> parseParcelsFromFile(String filePath) {
        var lines = fileLinesReader.readAllLines(filePath)
                .stream()
                .iterator();
        var parcels = new ArrayList<Parcel>();
        var rows = new ArrayList<List<Character>>();
        while (lines.hasNext()) {
            var currentLine = lines.next();
            if (currentLine.isBlank()){
                if (!rows.isEmpty()){
                    parcels.add(new Parcel(rows));
                }

                rows = new ArrayList<>();
                continue;
            }

            rows.add(currentLine.chars().mapToObj(c -> (char)c).collect(Collectors.toList()));
        }

        if (!rows.isEmpty()){
            parcels.add(new Parcel(rows));
        }


        return parcels;
    }
}
