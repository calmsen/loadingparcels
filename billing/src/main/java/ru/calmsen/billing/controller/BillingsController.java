package ru.calmsen.billing.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.calmsen.billing.command.impl.BillingCommand;
import ru.calmsen.billing.model.domain.Billing;
import ru.calmsen.billing.model.domain.enums.Period;
import ru.calmsen.billing.model.domain.enums.ViewFormat;
import ru.calmsen.billing.util.DateUtil;

import java.time.LocalDate;

/**
 * Контроллер для работы со счетами
 */
@RestController
@Tag(name = "Billings", description = "Операции со счетами")
@RequestMapping("api/v1/billing")
@RequiredArgsConstructor
public class BillingsController {
    private final BillingCommand billingCommand;

    /**
     * Детали счета
     *
     * @param user       идентификатор пользователя
     * @param from       дата от (включительно)
     * @param to         дата до (включительно)
     * @param viewFormat формат вывода
     * @return детали счета
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Детали счета",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Billing[].class))}),
            @ApiResponse(responseCode = "400", description = "Неверно передан запрос",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "422", description = "Запрос не прошёл проверку бизнес-правил",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "500", description = "Неопределенная ошибка на сервере",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})
    })
    @Operation(summary = "Детали счета")
    @GetMapping
    public ResponseEntity<String> billing(
            @Parameter(description = "Идентификатор пользователя") @RequestParam String user,
            @Parameter(description = "Дата от (включительно)") @RequestParam(required = false) @DateTimeFormat(pattern = DateUtil.DD_MM_YYYY) LocalDate from,
            @Parameter(description = "Дата до (включительно)") @RequestParam(required = false) @DateTimeFormat(pattern = DateUtil.DD_MM_YYYY) LocalDate to,
            @Parameter(description = "Период") @RequestParam(defaultValue = "NONE", required = false) Period period,
            @Parameter(description = "Формат вывода") @RequestParam(defaultValue = "JSON", required = false) ViewFormat viewFormat) {
        return ResponseEntity.ok(billingCommand.execute(
                BillingCommand.Context.builder()
                        .user(user)
                        .fromDate(from)
                        .toDate(to)
                        .period(period)
                        .viewFormat(viewFormat)
                        .build()
        ));
    }
}
