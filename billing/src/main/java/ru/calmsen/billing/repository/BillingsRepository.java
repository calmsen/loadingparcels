package ru.calmsen.billing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.calmsen.billing.model.domain.Billing;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillingsRepository  extends JpaRepository<Billing, Long> {
    List<Billing> findAllByUserAndDateBetweenOrderByDateDesc(String user, LocalDate dateAfter, LocalDate dateBefore);
}
