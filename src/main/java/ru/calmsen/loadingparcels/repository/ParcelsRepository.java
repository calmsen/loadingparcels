package ru.calmsen.loadingparcels.repository;

import ru.calmsen.loadingparcels.model.domain.Parcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с посылками.
 */
public class ParcelsRepository {
    private final List<Parcel> parcels = new ArrayList<>();

    /**
     * Возвращает все доступные посылки
     *
     * @return список посылок
     */
    public List<Parcel> findAllParcels() {
        return parcels;
    }

    /**
     * Находит посылку по наименованию
     *
     * @param name наименование посылки
     * @return контейнер с посылкой или пустой контейнер
     */
    public Optional<Parcel> findParcel(String name) {
        return parcels.stream().filter(x -> x.getName().equals(name))
                .findFirst();
    }

    /**
     * Добавляет посылку
     *
     * @param parcel объект посылки
     */
    public void addParcel(Parcel parcel) {
        parcels.add(parcel);
    }

    /**
     * Обновляет посылку
     *
     * @param parcel объект посылки
     */
    public void updateParcel(Parcel parcel) {
        deleteParcel(parcel.getName());
        parcels.add(parcel);
    }

    /**
     * Удаляет посылку
     *
     * @param parcelName наименование посылки
     */
    public void deleteParcel(String parcelName) {
        findParcel(parcelName).ifPresent(parcels::remove);
    }
}
