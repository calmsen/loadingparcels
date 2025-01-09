package ru.calmsen.loadingparcels.view;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.mapper.ParcelsMapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.dto.WithCountParcelDto;
import ru.calmsen.loadingparcels.util.JsonUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Json представление для списка посылок с подсчетом
 */
@RequiredArgsConstructor
public class JsonWithCountParcelsView implements ParcelsView {
    private final ParcelsMapper parcelsMapper;

    /**
     * Возвращает данные для представления списка посылок с подсчетом в json.
     *
     * @param parcels список посылок
     * @return данные в виде json
     */
    public String getOutputData(List<Parcel> parcels) {
        var parcelsGroup = parcels.stream().collect(
                Collectors.groupingBy(Parcel::getName));

        var withCountParcels = parcelsGroup
                .values()
                .stream()
                .map(parcelList -> WithCountParcelDto
                        .builder()
                        .parcel(parcelsMapper.toParcelDto(parcelList.getFirst()))
                        .quantity(parcelList.size()).build())
                .toList();

        return JsonUtil.toJson(withCountParcels);
    }
}
