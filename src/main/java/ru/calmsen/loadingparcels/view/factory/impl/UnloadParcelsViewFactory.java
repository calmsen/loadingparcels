package ru.calmsen.loadingparcels.view.factory.impl;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.mapper.ParcelsMapper;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.view.*;
import ru.calmsen.loadingparcels.view.factory.ParcelsViewFactory;
import ru.calmsen.loadingparcels.view.impl.CsvWithCountParcelsView;
import ru.calmsen.loadingparcels.view.impl.JsonWithCountParcelsView;
import ru.calmsen.loadingparcels.view.impl.TxtWithCountParcelsView;

/**
 * Фабрика для создания представления для списка посылок при разгрузке
 */
@RequiredArgsConstructor
public class UnloadParcelsViewFactory implements ParcelsViewFactory {
    private final DefaultParcelsViewFactory defaultParcelsViewFactory;
    private final ParcelsMapper parcelsMapper;

    /**
     * Создает представление для списка посылок для указанного формата
     *
     * @param format формат представления
     * @return представление для списка посылок
     */
    public ParcelsView createView(ViewFormat format) {
        return defaultParcelsViewFactory.createView(format);
    }

    /**
     * Создает представление для списка посылок для указанного формата с учетом данных контекста
     *
     * @param format формат представления
     * @return представление для списка посылок
     */
    public ParcelsView createView(ViewFormat format, Context context) {
        if (!context.withCount){
            return createView(format);
        }

        return switch (format) {
            case TXT -> new TxtWithCountParcelsView();
            case JSON -> new JsonWithCountParcelsView(parcelsMapper);
            case CSV -> new CsvWithCountParcelsView();
        };
    }
}
