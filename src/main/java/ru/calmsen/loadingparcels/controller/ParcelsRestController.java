package ru.calmsen.loadingparcels.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.calmsen.loadingparcels.command.CommandParameter;
import ru.calmsen.loadingparcels.command.impl.*;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;
import ru.calmsen.loadingparcels.model.domain.enums.ViewFormat;
import ru.calmsen.loadingparcels.model.dto.ParcelDto;

@RestController
@Tag(name = "Parcels", description = "Операции с посылками")
@RequestMapping("api/v1/parcels")
@RequiredArgsConstructor
public class ParcelsRestController {
    private final FindParcelCommand findParcelCommand;
    private final CreateParcelCommand createParcelCommand;
    private final UpdateParcelCommand updateParcelCommand;
    private final DeleteParcelCommand deleteParcelCommand;
    private final LoadParcelsCommand loadParcelsCommand;
    private final UnloadParcelsCommand unloadParcelsCommand;

    /**
     * Найти посылки
     *
     * @param viewFormat формат вывода
     * @param pageNumber номер страницы
     * @param pageSize   кол-во посылок на странице
     * @return список посылок
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список посылок",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Parcel[].class))}),
            @ApiResponse(responseCode = "400", description = "Неверно передан запрос",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "422", description = "Запрос не прошёл проверку бизнес-правил",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "500", description = "Неопределенная ошибка на сервере",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})
    })
    @Operation(summary = "Список посылок")
    @GetMapping
    public String findParcels(
            @Parameter(description = "Формат вывода") @RequestParam(defaultValue = "JSON", required = false) ViewFormat viewFormat,
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = CommandParameter.FindParcel.PAGE_NUMBER_DEFAULT_VALUE, required = false) int pageNumber,
            @Parameter(description = "Кол-во посылок на странице") @RequestParam(defaultValue = CommandParameter.FindParcel.PAGE_SIZE_DEFAULT_VALUE, required = false) int pageSize) {
        return findParcelCommand.execute(new FindParcelCommand.Context(null, viewFormat, pageNumber, pageSize));
    }

    /**
     * Найти посылку
     *
     * @param name       название посылки
     * @param viewFormat формат вывода
     * @return список посылок
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Объект посылки",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Parcel.class))}),
            @ApiResponse(responseCode = "400", description = "Неверно передан запрос",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "422", description = "Запрос не прошёл проверку бизнес-правил",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "500", description = "Неопределенная ошибка на сервере",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})
    })
    @Operation(summary = "Найти посылку")
    @GetMapping("{name}")
    public String findParcel(
            @Parameter(description = "Название посылки") @PathVariable String name,
            @Parameter(description = "Формат вывода") @RequestParam(defaultValue = "JSON", required = false) ViewFormat viewFormat
    ) {
        return findParcelCommand.execute(new FindParcelCommand.Context(name, viewFormat, 1, 1));
    }

    /**
     * Добавление посылки
     *
     * @param parcel объект посылки
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "Неверно передан запрос",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "422", description = "Запрос не прошёл проверку бизнес-правил",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "500", description = "Неопределенная ошибка на сервере",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})
    })
    @Operation(summary = "Добавление посылки")
    @PostMapping
    public ResponseEntity<Void> createParcel(@Parameter(description = "Объект посылки") @RequestBody ParcelDto parcel) {
        createParcelCommand.execute(parcel);
        return ResponseEntity.noContent().build();
    }

    /**
     * Редактирование посылки
     *
     * @param name   название посылки
     * @param parcel объект посылки
     * @return пустой результат или информация об ошибке
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "Неверно передан запрос",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "422", description = "Запрос не прошёл проверку бизнес-правил",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "500", description = "Неопределенная ошибка на сервере",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})
    })
    @Operation(summary = "Редактирование посылки")
    @PutMapping("{name}")
    public ResponseEntity<Void> updateParcel(
            @Parameter(description = "Название посылки") @PathVariable String name,
            @Parameter(description = "Объект посылки") @RequestBody ParcelDto parcel) {
        parcel.setName(name);
        updateParcelCommand.execute(parcel);
        return ResponseEntity.noContent().build();

    }

    /**
     * Удаление посылки
     *
     * @param name названия посылки
     * @return пустой результат или информация об ошибке
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "Неверно передан запрос",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "422", description = "Запрос не прошёл проверку бизнес-правил",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "500", description = "Неопределенная ошибка на сервере",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})
    })
    @Operation(summary = "Удаление посылки")
    @DeleteMapping("{name}")
    public ResponseEntity<Void> deleteParcel(@Parameter(description = "Название посылки") @PathVariable String name) {
        deleteParcelCommand.execute(name);
        return ResponseEntity.noContent().build();

    }

    /**
     * Погрузка посылок
     *
     * @param request объект запроса
     * @return список с загруженными машинами
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список с загруженными машинами",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Truck[].class))}),
            @ApiResponse(responseCode = "400", description = "Неверно передан запрос",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "422", description = "Запрос не прошёл проверку бизнес-правил",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "500", description = "Неопределенная ошибка на сервере",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})
    })
    @Operation(summary = "Погрузка посылок")
    @PostMapping("/actions/load")
    public String loadParcels(@Parameter(description = "Объект запроса погрузки") @RequestBody LoadParcelsCommand.Context request) {
        request.setViewFormat(
                ObjectUtils.firstNonNull(request.getViewFormat(), ViewFormat.JSON)
        );
        request.setLoadingMode(
                ObjectUtils.firstNonNull(request.getLoadingMode(), LoadingMode.ONEPARCEL)
        );

        return loadParcelsCommand.execute(request);
    }

    /**
     * Разгрузка посылок
     *
     * @param request объект запроса
     * @return список с посылками
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список с посылками",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Truck[].class))}),
            @ApiResponse(responseCode = "400", description = "Неверно передан запрос",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "422", description = "Запрос не прошёл проверку бизнес-правил",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))}),
            @ApiResponse(responseCode = "500", description = "Неопределенная ошибка на сервере",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))})
    })
    @Operation(summary = "Разгрузка посылок")
    @PostMapping("/actions/unload")
    public String unloadParcels(@Parameter(description = "Объект запроса разгрузки") @RequestBody UnloadParcelsCommand.Context request) {
        request.setViewFormat(
                ObjectUtils.firstNonNull(request.getViewFormat(), ViewFormat.JSON)
        );
        return unloadParcelsCommand.execute(request);
    }
}
