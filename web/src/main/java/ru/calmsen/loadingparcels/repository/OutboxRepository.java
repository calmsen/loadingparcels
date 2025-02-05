package ru.calmsen.loadingparcels.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.calmsen.loadingparcels.model.domain.OutboxMessage;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {
}

