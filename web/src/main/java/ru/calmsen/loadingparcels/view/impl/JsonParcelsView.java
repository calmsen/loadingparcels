package ru.calmsen.loadingparcels.view.impl;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.mapper.ParcelsMapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.util.JsonUtil;
import ru.calmsen.loadingparcels.view.ParcelsView;

import java.util.List;

/**
 * Json представление для списка посылок
 */
@RequiredArgsConstructor
public class JsonParcelsView implements ParcelsView {
    private final ParcelsMapper parcelsMapper;

    /**
     * Возвращает данные для представления списка посылок в json.
     *
     * @param parcels список посылок
     * @return данные в виде json
     */
    @Override
    public String buildOutputData(List<Parcel> parcels) {
        var parcelsDto = parcelsMapper.toParcelsDto(parcels);
        return JsonUtil.toJson(parcelsDto);
    }
}
