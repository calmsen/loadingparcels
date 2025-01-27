package ru.calmsen.loadingparcels.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.calmsen.loadingparcels.model.domain.Billing;
import ru.calmsen.loadingparcels.repository.constant.BillingQuery;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BillingsRepository {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Добавить счет
     *
     * @param billing счет
     */
    public void addBilling(Billing billing) {
        jdbcTemplate.update(BillingQuery.INSERT,
                billing.getUser(),
                billing.getDescription(),
                billing.getType(),
                Timestamp.valueOf(billing.getDate().atTime(LocalTime.MIDNIGHT)),
                billing.getQuantity(),
                billing.getCost()
        );
    }

    /**
     * Получить детали счетов
     *
     * @param user     идентификатор пользователя
     * @param fromDate дата от (включительно)
     * @param toDate   дата до (включительно)
     * @return список счетов
     */
    public List<Billing> getBillings(String user, LocalDate fromDate, LocalDate toDate) {
        return jdbcTemplate.query(BillingQuery.FIND_ALL, this::toBilling, user, fromDate, toDate)
                .stream()
                .sorted(Comparator.comparing(Billing::getDate).reversed())
                .toList();
    }

    private Billing toBilling(ResultSet rs, int rowNum) throws SQLException {
        return new Billing(
                rs.getString("user"),
                rs.getString("description"),
                rs.getString("type"),
                rs.getTimestamp("date").toLocalDateTime().toLocalDate(),
                rs.getInt("quantity"),
                rs.getBigDecimal("cost")
        );
    }
}
