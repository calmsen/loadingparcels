package ru.calmsen.loadingparcels.view.factory;

import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.view.TrucksView;

/**
 * Фабрика для создания представления для списка машин
 */
public interface TrucksViewFactory {
    TrucksView createView(ViewFormat format);
}
