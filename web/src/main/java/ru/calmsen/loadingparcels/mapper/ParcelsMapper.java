package ru.calmsen.loadingparcels.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.dto.ParcelDto;

import java.util.List;
import java.util.Map;

/**
 * Маппер для моделей parcel.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class ParcelsMapper {
    public Parcel toParcel(ParcelDto parcelDto) {
        return new Parcel(
                parcelDto.getName(),
                parcelDto.getForm(),
                parcelDto.getSymbol()
        );
    }

    public abstract ParcelDto toParcelDto(Map<String, String> map);

    public abstract ParcelDto toParcelDto(Parcel parcel);

    public abstract List<ParcelDto> toParcelsDto(List<Parcel> parcels);
}
