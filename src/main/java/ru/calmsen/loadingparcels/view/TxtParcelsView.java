package ru.calmsen.loadingparcels.view;

import ru.calmsen.loadingparcels.model.domain.Parcel;

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
    public String getOutputData(List<Parcel> parcels) {
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
