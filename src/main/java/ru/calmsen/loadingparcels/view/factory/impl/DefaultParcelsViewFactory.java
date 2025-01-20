package ru.calmsen.loadingparcels.view.factory.impl;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.mapper.ParcelsMapper;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.view.impl.CsvParcelsView;
import ru.calmsen.loadingparcels.view.impl.JsonParcelsView;
import ru.calmsen.loadingparcels.view.ParcelsView;
import ru.calmsen.loadingparcels.view.impl.TxtParcelsView;
import ru.calmsen.loadingparcels.view.factory.ParcelsViewFactory;

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
