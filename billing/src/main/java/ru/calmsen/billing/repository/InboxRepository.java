package ru.calmsen.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.calmsen.billing.model.domain.InboxMessage;

import java.util.UUID;

@Repository
public interface InboxRepository extends JpaRepository<InboxMessage, UUID> {
}
