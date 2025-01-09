package ru.calmsen.loadingparcels.view;

import ru.calmsen.loadingparcels.model.domain.Parcel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Csv представление для списка посылок с подсчетом
 */
public class CsvWithCountParcelsView implements ParcelsView {
    /**
     * Возвращает данные для представления списка посылок с подсчетом в csv.
     *
     * @param parcels список посылок
     * @return данные в виде csv
     */
    @Override
    public String getOutputData(List<Parcel> parcels) {
        var parcelsGroup = parcels.stream().collect(
                Collectors.groupingBy(Parcel::getName));
        var output = new StringBuilder("Имя;Форма;Символ;Количество");
        for (var item : parcelsGroup.entrySet()) {
            var parcel = item.getValue().getFirst();
            output.append("\n").append(parcel.getName());
            output.append(";").append(parcel.getForm().replace("\n", "\\n"));
            output.append(";").append(parcel.getSymbol());
            output.append(";").append(item.getValue().size());
        }

        return output.toString();
    }
}
