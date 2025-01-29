package ru.calmsen.loadingparcels.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.calmsen.loadingparcels.command.CommandParameter;
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
     * @param pageNumber номер страницы
     * @param pageSize кол-во посылок на странице
     * @return список посылок
     */
    @ShellMethod("Найти посылку")
    public String find(
            @ShellOption(defaultValue = "", help = "Название посылки") String name,
            @ShellOption(value = CommandParameter.FindParcel.OUT_FORMAT, defaultValue = CommandParameter.FindParcel.OUT_FORMAT_DEFAULT_VALUE, help = "Формат вывода") String outFormat,
            @ShellOption(value = CommandParameter.FindParcel.PAGE_NUMBER, defaultValue = CommandParameter.FindParcel.PAGE_NUMBER_DEFAULT_VALUE, help = "Номер страницы") String pageNumber,
            @ShellOption(value = CommandParameter.FindParcel.PAGE_SIZE, defaultValue = CommandParameter.FindParcel.PAGE_SIZE_DEFAULT_VALUE, help = "Количество посылок на странице") String pageSize
    ) {
        return findParcelCommand.execute(Map.of(
            CommandParameter.FindParcel.NAME, name,
            CommandParameter.FindParcel.OUT_FORMAT, outFormat,
            CommandParameter.FindParcel.PAGE_NUMBER, pageNumber,
            CommandParameter.FindParcel.PAGE_SIZE, pageSize
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
            CommandParameter.CreateParcel.NAME, name,
            CommandParameter.CreateParcel.FORM, form,
            CommandParameter.CreateParcel.SYMBOL, symbol
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
            CommandParameter.UpdateParcel.NAME, name,
            CommandParameter.UpdateParcel.FORM, form,
            CommandParameter.UpdateParcel.SYMBOL, symbol
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
            CommandParameter.DeleteParcel.NAME, name
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
            @ShellOption String inFile,
            @ShellOption(value = CommandParameter.LoadParcels.PARCEL_NAMES, defaultValue = "") String parcelNames,
            @ShellOption(value = CommandParameter.LoadParcels.OUT_FILE, defaultValue = "") String outFile,
            @ShellOption(value = CommandParameter.LoadParcels.OUT_FORMAT, defaultValue = CommandParameter.LoadParcels.OUT_FORMAT_DEFAULT_VALUE) String outFormat,
            @ShellOption(value = CommandParameter.LoadParcels.LOADING_MODE, defaultValue = CommandParameter.LoadParcels.LOADING_MODE_DEFAULT_VALUE) String loadingMode,
            @ShellOption(value = CommandParameter.LoadParcels.TRUCKS, defaultValue = "") String trucks,
            @ShellOption(value = CommandParameter.LoadParcels.TRUCKS_COUNT, defaultValue = CommandParameter.LoadParcels.TRUCKS_COUNT_DEFAULT_VALUE) String trucksCount,
            @ShellOption(value = CommandParameter.LoadParcels.TRUCKS_WIDTH, defaultValue = CommandParameter.LoadParcels.TRUCKS_WIDTH_DEFAULT_VALUE) String trucksWidth,
            @ShellOption(value = CommandParameter.LoadParcels.TRUCKS_HEIGHT, defaultValue = CommandParameter.LoadParcels.TRUCKS_HEIGHT_DEFAULT_VALUE) String trucksHeight,
            @ShellOption(value = CommandParameter.USER, defaultValue = "") String user
    ) {
        return loadParcelsCommand.execute(Map.of(
            CommandParameter.LoadParcels.IN_FILE, inFile,
            CommandParameter.LoadParcels.PARCEL_NAMES, parcelNames,
            CommandParameter.LoadParcels.OUT_FILE, outFile,
            CommandParameter.LoadParcels.OUT_FORMAT, outFormat,
            CommandParameter.LoadParcels.LOADING_MODE, loadingMode,
            CommandParameter.LoadParcels.TRUCKS, trucks,
            CommandParameter.LoadParcels.TRUCKS_COUNT, trucksCount,
            CommandParameter.LoadParcels.TRUCKS_WIDTH, trucksWidth,
            CommandParameter.LoadParcels.TRUCKS_HEIGHT, trucksHeight,
            CommandParameter.USER, user
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
            @ShellOption String inFile,
            @ShellOption(value = CommandParameter.UnloadParcels.OUT_FORMAT, defaultValue = CommandParameter.UnloadParcels.OUT_FORMAT_DEFAULT_VALUE) String outFormat,
            @ShellOption(value = CommandParameter.UnloadParcels.WITH_COUNT, defaultValue = CommandParameter.UnloadParcels.WITH_COUNT_DEFAULT_VALUE) String withCount,
            @ShellOption(value = CommandParameter.USER, defaultValue = "") String user
    ) {
        return unloadParcelsCommand.execute(Map.of(
            CommandParameter.UnloadParcels.IN_FILE, inFile,
            CommandParameter.UnloadParcels.OUT_FORMAT, outFormat,
            CommandParameter.UnloadParcels.WITH_COUNT, withCount,
            CommandParameter.USER, user
        ));
    }
}
