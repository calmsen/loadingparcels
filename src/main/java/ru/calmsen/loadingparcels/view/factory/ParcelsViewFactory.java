package ru.calmsen.loadingparcels.view.factory;

import lombok.Builder;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.view.ParcelsView;

/**
 * Фабрика для создания представления для списка посылок
 */
public interface ParcelsViewFactory {
    ParcelsView createView(ViewFormat format);

    default ParcelsView createView(ViewFormat format, Context context){
        return createView(format);
    }

    @Builder
    class Context{
       public final boolean withCount;
    }
}
