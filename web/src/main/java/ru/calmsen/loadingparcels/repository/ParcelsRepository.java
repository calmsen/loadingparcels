package ru.calmsen.loadingparcels.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.calmsen.loadingparcels.model.domain.Parcel;

/**
 * Репозиторий для работы с посылками.
 */
@Repository
public interface ParcelsRepository extends JpaRepository<Parcel, String> {
}
