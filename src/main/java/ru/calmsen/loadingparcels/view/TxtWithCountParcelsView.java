package ru.calmsen.loadingparcels.view;

import ru.calmsen.loadingparcels.model.domain.Parcel;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Текстовое представление для списка посылок с подсчетом
 */
public class TxtWithCountParcelsView implements ParcelsView {
    /**
     * Возвращает данные для представления списка посылок с подсчетом в txt.
     *
     * @param parcels список посылок
     * @return данные в виде txt
     */
    @Override
    public String getOutputData(List<Parcel> parcels) {
        var parcelsGroup = parcels.stream().collect(
                Collectors.groupingBy(Parcel::getName));
        var output = new StringBuilder();
        for (var item : parcelsGroup.entrySet()) {
            if (!output.isEmpty()) {
                output.append("\n\n");
            }

            output.append(item.getValue().getFirst().toString());
            output.append("\nquantity: ").append(item.getValue().size());
        }

        return output.toString();
    }
}
