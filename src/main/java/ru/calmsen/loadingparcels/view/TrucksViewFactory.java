package ru.calmsen.loadingparcels.view;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.mapper.TrucksMapper;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;

@RequiredArgsConstructor
public class TrucksViewFactory {
    private final TrucksMapper trucksMapper;
    public TrucksView createView(ViewFormat format) {
        switch (format) {
            case TXT -> {
                return new TxtTrucksView();
            }
            case JSON -> {
                return new JsonTrucksView(trucksMapper);
            }
        }
        throw new IllegalArgumentException("Нет реализации TrucksView с форматом " + format);
    }
}
