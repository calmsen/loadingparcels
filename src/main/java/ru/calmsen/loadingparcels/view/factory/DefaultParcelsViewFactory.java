package ru.calmsen.loadingparcels.view.factory;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.mapper.ParcelsMapper;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.view.CsvParcelsView;
import ru.calmsen.loadingparcels.view.JsonParcelsView;
import ru.calmsen.loadingparcels.view.ParcelsView;
import ru.calmsen.loadingparcels.view.TxtParcelsView;

/**
 * Фабрика по умолчанию для создания представления для списка посылок
 */
@RequiredArgsConstructor
public class DefaultParcelsViewFactory implements ParcelsViewFactory {
    private final ParcelsMapper parcelsMapper;

    /**
     * Создает представление для списка посылок для указанного формата
     *
     * @param format формат представления
     * @return представление для списка посылок
     */
    public ParcelsView createView(ViewFormat format) {
        return switch (format) {
            case TXT -> new TxtParcelsView();
            case JSON -> new JsonParcelsView(parcelsMapper);
            case CSV -> new CsvParcelsView();
        };
    }
}
