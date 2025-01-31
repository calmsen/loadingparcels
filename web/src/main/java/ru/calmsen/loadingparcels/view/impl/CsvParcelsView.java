package ru.calmsen.loadingparcels.view.impl;

import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.view.ParcelsView;

import java.util.List;

/**
 * Csv представление для списка посылок
 */
public class CsvParcelsView implements ParcelsView {
    /**
     * Возвращает данные для представления списка посылок в csv.
     *
     * @param parcels список посылок
     * @return данные в виде csv
     */
    @Override
    public String buildOutputData(List<Parcel> parcels) {
        var output = new StringBuilder("Имя;Форма;Символ");
        for (Parcel parcel : parcels) {
            output.append("\n").append(parcel.getName());
            output.append(";").append(parcel.getForm().replace("\n", "\\n"));
            output.append(";").append(parcel.getSymbol());
        }

        return output.toString();
    }
}
