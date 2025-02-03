package ru.calmsen.loadingparcels.view.impl;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.mapper.TrucksMapper;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.util.JsonUtil;
import ru.calmsen.loadingparcels.view.TrucksView;

import java.util.List;

/**
 * Json представление для списка машин
 */
@RequiredArgsConstructor
public class JsonTrucksView implements TrucksView {
    private final TrucksMapper trucksMapper;

    /**
     * Возвращает данные для представления списка машин в json.
     *
     * @param trucks список машин
     * @return данные в виде json
     */
    public String buildOutputData(List<Truck> trucks) {
        var trucksDto = trucksMapper.toTrucksDto(trucks);
        return JsonUtil.toJson(trucksDto);
    }
}
