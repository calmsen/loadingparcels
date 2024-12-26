package ru.calmsen.loadingparcels.view;

import lombok.RequiredArgsConstructor;
import ru.calmsen.loadingparcels.mapper.TrucksMapper;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.util.JsonUtil;

import java.util.List;

@RequiredArgsConstructor
public class JsonTrucksView implements TrucksView {
    private final TrucksMapper trucksMapper;

    public String getOutputData(List<Truck> trucks) {
        var trucksDto = trucksMapper.toTrucksDto(trucks);
        return JsonUtil.toJson(trucksDto);
    }
}
