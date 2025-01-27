package ru.calmsen.loadingparcels.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.repository.constant.ParcelQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с посылками.
 */
@Repository
@RequiredArgsConstructor
public class ParcelsRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Возвращает все доступные посылки
     *
     * @param pageNumber номер страницы
     * @param pageSize кол-во посылок на странице
     * @return список посылок
     */
    public List<Parcel> findAllParcels(int pageNumber, int pageSize) {
        if (pageNumber <= 0) {
            pageNumber = 1;
        }

        if (pageSize <= 0) {
            pageSize = 1;
        }

        return jdbcTemplate.query(ParcelQuery.FIND_ALL, this::toParcel, pageSize, (pageNumber - 1) * pageSize);
    }

    /**
     * Находит посылку по наименованию
     *
     * @param name наименование посылки
     * @return контейнер с посылкой или пустой контейнер
     */
    public Optional<Parcel> findParcel(String name) {
        return jdbcTemplate.query(ParcelQuery.FIND_BY_NAME, this::toParcel, name)
                .stream().findFirst();
    }

    /**
     * Добавляет посылку
     *
     * @param parcel объект посылки
     */
    public void addParcel(Parcel parcel) {
        jdbcTemplate.update(ParcelQuery.INSERT, parcel.getName(), parcel.getForm(), String.valueOf(parcel.getSymbol()));
    }

    /**
     * Обновляет посылку
     *
     * @param parcel объект посылки
     */
    public void updateParcel(Parcel parcel) {
        jdbcTemplate.update(ParcelQuery.UPDATE, parcel.getForm(), String.valueOf(parcel.getSymbol()), parcel.getName());
    }

    /**
     * Удаляет посылку
     *
     * @param parcelName наименование посылки
     */
    public void deleteParcel(String parcelName) {
        findParcel(parcelName).ifPresent(
                parcel -> jdbcTemplate.update(ParcelQuery.DELETE, parcelName)
        );
    }

    private Parcel toParcel(ResultSet resultSet, int rowNum) throws SQLException {
        return new Parcel(
                resultSet.getString("name"),
                resultSet.getString("form"),
                resultSet.getString("symbol").charAt(0)
        );
    }
}
