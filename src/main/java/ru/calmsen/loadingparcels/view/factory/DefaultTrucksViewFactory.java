package ru.calmsen.loadingparcels.view.factory;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.mapper.TrucksMapper;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.view.JsonTrucksView;
import ru.calmsen.loadingparcels.view.TrucksView;
import ru.calmsen.loadingparcels.view.TxtTrucksView;

/**
 * Фабрика по умолчанию для создания представления для списка машин
 */
@RequiredArgsConstructor
public class DefaultTrucksViewFactory implements TrucksViewFactory {
    private final TrucksMapper trucksMapper;

    /**
     * Создает представление для списка машин для указанного формата
     *
     * @param format формат представления
     * @return представление для списка посылок
     */
    public TrucksView createView(ViewFormat format) {
        return switch (format) {
            case TXT -> new TxtTrucksView();
            case JSON -> new JsonTrucksView(trucksMapper);
            default -> throw new IllegalArgumentException("Нет реализации TrucksView с форматом " + format);
        };
    }
}
