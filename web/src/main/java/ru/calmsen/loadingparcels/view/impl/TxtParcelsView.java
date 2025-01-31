package ru.calmsen.loadingparcels.view.impl;

import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.view.ParcelsView;

import java.util.List;

/**
 * Текстовое представление для списка посылок
 */
public class TxtParcelsView implements ParcelsView {
    /**
     * Возвращает данные для представления списка посылок в txt.
     *
     * @param parcels список посылок
     * @return данные в виде txt
     */
    @Override
    public String buildOutputData(List<Parcel> parcels) {
        var output = new StringBuilder();
        for (Parcel parcel : parcels) {
            if (!output.isEmpty()) {
                output.append("\n\n");
            }

            output.append(parcel.toString());
        }

        return output.toString();
    }
}
