package ru.calmsen.loadingparcels.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.calmsen.loadingparcels.command.CommandSender;
import ru.calmsen.loadingparcels.command.impl.*;

import java.util.Map;

@ShellComponent
@RequiredArgsConstructor
public class ParcelsController {
    private final FindParcelCommand findParcelCommand;
    private final CreateParcelCommand createParcelCommand;
    private final UpdateParcelCommand updateParcelCommand;
    private final DeleteParcelCommand deleteParcelCommand;
    private final LoadParcelsCommand loadParcelsCommand;
    private final UnloadParcelsCommand unloadParcelsCommand;

    /**
     * Найти посылку (и)
     *
     * @param name      название посылки. Если не указано, то выбираются все посылки
     * @param outFormat формат вывода
     * @return список посылок
     */
    @ShellMethod("Найти посылку")
    public String find(
            @ShellOption(defaultValue = "", help = "Название посылки") String name,
            @ShellOption(value = "out-format", defaultValue = "TXT", help = "Формат вывода") String outFormat
    ) {
        return findParcelCommand.execute(Map.of(
        "find", name,
        "out-format", outFormat
        ));
    }

    /**
     * Добавление посылки
     *
     * @param name   названия посылки
     * @param form   форма посылки
     * @param symbol Символ для отображения посылки
     * @return пустой результат или информация об ошибке
     */
    @ShellMethod("Добавление посылки")
    public String create(
            @ShellOption(help = "Название посылки") String name,
            @ShellOption(help = "Форма посылки") String form,
            @ShellOption(help = "Символ для отображения посылки") String symbol
    ) {
        return createParcelCommand.execute(Map.of(
        "name", name,
        "form", form,
        "symbol", symbol
        ));
    }

    /**
     * Редактирование посылки
     *
     * @param name   названия посылки
     * @param form   форма посылки
     * @param symbol Символ для отображения посылки
     * @return пустой результат или информация об ошибке
     */
    @ShellMethod("Редактирование посылки")
    public String update(
            @ShellOption(help = "Название посылки") String name,
            @ShellOption(help = "Форма посылки") String form,
            @ShellOption(help = "Символ для отображения посылки") String symbol
    ) {
        return updateParcelCommand.execute(Map.of(
                "name", name,
                "form", form,
                "symbol", symbol
        ));
    }

    /**
     * Удаление посылки
     *
     * @param name названия посылки
     * @return пустой результат или информация об ошибке
     */
    @ShellMethod("Удаление посылки")
    public String delete(@ShellOption(help = "Название посылки") String name) {
        return deleteParcelCommand.execute(Map.of(
        "delete", name
        ));
    }

    /**
     * Погрузка машин. На вход идет список с названиями посылок. На выходе список с загруженными машинами.
     *
     * @param inFile       название входного файла
     * @param outFile      название выходного файла
     * @param outFormat    формат вывода
     * @param loadingMode  тип погрузки
     * @param trucks       размеры машин
     * @param trucksCount  количество машин. Игнорируется если --trucks не пустой
     * @param trucksWidth  ширина кузова машины. Игнорируется если --trucks не пустой
     * @param trucksHeight высота кузова машины. Игнорируется если --trucks не пустой
     * @param user         идентификатор пользователя
     * @return список с загруженными машинами
     */
    @ShellMethod("Погрузка машин")
    public String load(
            @ShellOption("in-file") String inFile,
            @ShellOption(value = "out-file", defaultValue = "") String outFile,
            @ShellOption(value = "out-format", defaultValue = "TXT") String outFormat,
            @ShellOption(value = "loading-mode", defaultValue = "ONEPARCEL") String loadingMode,
            @ShellOption(value = "trucks", defaultValue = "") String trucks,
            @ShellOption(value = "trucks-count", defaultValue = "") String trucksCount,
            @ShellOption(value = "trucks-width", defaultValue = "") String trucksWidth,
            @ShellOption(value = "trucks-height", defaultValue = "") String trucksHeight,
            @ShellOption(value = "user", defaultValue = "") String user
    ) {
        return loadParcelsCommand.execute(Map.of(
        "load", inFile,
        "out-file", outFile,
        "out-format", outFormat,
        "loading-mode", loadingMode,
        "trucks", trucks,
        "trucks-count", trucksCount,
        "trucks-width", trucksWidth,
        "trucks-height", trucksHeight,
        "user", user
        ));
    }

    /**
     * Разгрузка машин. На вход идет json файл с загруженными машинами. На выходе список с посылками.
     *
     * @param inFile    название входного файла
     * @param outFormat формат вывода
     * @param withCount выводить с подсчетом посылок
     * @param user      идентификатор пользователя
     * @return список с посылками
     */
    @ShellMethod("Разгрузка машин")
    public String unload(
            @ShellOption("in-file") String inFile,
            @ShellOption(value = "out-format", defaultValue = "TXT") String outFormat,
            @ShellOption(value = "with-count", defaultValue = "false") String withCount,
            @ShellOption(value = "user", defaultValue = "") String user
    ) {
        return unloadParcelsCommand.execute(Map.of(
        "unload", inFile,
        "out-format", outFormat,
        "with-count", withCount,
        "user", user
        ));
    }
}
