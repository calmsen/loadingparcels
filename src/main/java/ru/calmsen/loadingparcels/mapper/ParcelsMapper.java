package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.Mapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.dto.ParcelDto;

import java.util.List;
import java.util.Map;

/**
 * Маппер для моделей parcel.
 */
@Mapper
public abstract class ParcelsMapper {
    public Parcel toParcel(Map<String, String> map) {
        return new Parcel(
                map.get(Parcel.Fields.name),
                map.get(Parcel.Fields.form),
                map.get(Parcel.Fields.symbol).charAt(0)
        );
    }

    public abstract ParcelDto toParcelDto(Parcel parcel);

    public abstract List<ParcelDto> toParcelsDto(List<Parcel> parcels);
}
