package calmsen.util.parcelsparser;

import calmsen.exception.ParcelParserException;
import calmsen.model.domain.Parcel;
import calmsen.util.FileLinesReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ParcelsParser {
    private final FileLinesReader fileLinesReader;
    private final List<ParcelParser> parcelParsers;

    public List<Parcel> parseParcelsFromFile(String filePath) {
        var lines = fileLinesReader.readAllLines(filePath)
                .stream()
                .iterator();
        var parcels = new ArrayList<Parcel>();
        while (lines.hasNext()) {
            var currentLine = lines.next();
            if (currentLine.isBlank()){
                continue;
            }

            var parcel = parseParcel(lines, currentLine);
            if (parcel == null) {
                throw new ParcelParserException(String.format("Не удалось распарсить посылку. Текущая строка: %s", currentLine));
            }

            parcels.add(parcel);

            // посылки должны разделяться пустой строкой
            if (lines.hasNext() && !lines.next().isBlank()) {
                throw new ParcelParserException("Неправильный формат посылок. Посылки должны разделяться пустой строкой.");
            }
        }

        return parcels;
    }

    private Parcel parseParcel(Iterator<String> lines, String currentLine) {
        for (ParcelParser p : parcelParsers) {
            var parcel = p.parse(lines, currentLine);
            if (parcel != null) {
                return parcel;
            }
        }

        return null;
    }
}
