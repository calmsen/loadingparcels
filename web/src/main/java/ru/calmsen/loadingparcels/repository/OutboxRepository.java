package ru.calmsen.loadingparcels.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.calmsen.loadingparcels.model.domain.OutboxMessage;

import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {
    List<OutboxMessage> findByOrderByCreatedAtAsc(Pageable pageable);
}

