package ru.calmsen.loadingparcels.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.exception.ParcelValidatorException;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.PlacedParcel;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;
import ru.calmsen.loadingparcels.repository.ParcelsRepository;
import ru.calmsen.loadingparcels.service.loadingalgorithm.LoadingAlgorithmFactory;
import ru.calmsen.loadingparcels.service.parser.ParcelsParser;
import ru.calmsen.loadingparcels.service.parser.TrucksParser;
import ru.calmsen.loadingparcels.util.FileReader;
import ru.calmsen.loadingparcels.validator.ParcelValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с посылками
 */
@Slf4j
@RequiredArgsConstructor
public class ParcelsService {
    private final ParcelsParser parcelsParser;
    private final TrucksParser trucksParser;
    private final ParcelValidator parcelValidator;
    private final LoadingAlgorithmFactory loadingAlgorithmFactory;
    private final ParcelsRepository parcelsRepository;
    private final FileReader fileReader;

    /**
     * Инициализирует доступный список посылок, расположенных в файле.
     *
     * @param fileName наименование файла.
     */
    public void initParcels(String fileName) {
        var parcels = parcelsParser.parseParcelsFromFile(fileName);
        validateParcels(parcels);
        for (var parcel : parcels) {
            parcelsRepository.addParcel(parcel);
        }
    }

    /**
     * Загружает посылки в машины. Наименования посылок можно передать в списке или в файле.
     *
     * @param parcelNames список наименований посылок, разделенных \n
     * @param fileName    наименование файла со списком посылок. Игнорируется если parcelNames не пустой
     * @param loadingMode тип погрузки
     * @param trucks      список машин
     */
    public void loadParcels(List<String> parcelNames, String fileName, LoadingMode loadingMode, List<Truck> trucks) {
        parcelNames = readParcelNamesFromFileIfEmpty(parcelNames, fileName);
        var parcels = findParcels(parcelNames);

        var loadingAlgorithm = loadingAlgorithmFactory.Create(loadingMode);
        loadingAlgorithm.loadParcels(parcels, trucks);
    }

    /**
     * Разгружает посылки
     *
     * @param fileName наименование файла со списком загруженных машин
     * @return список посылок
     */
    public List<Parcel> unloadTrucks(String fileName) {
        var trucks = trucksParser.parseTrucksFromFile(fileName);
        return trucks.stream()
                .flatMap(truck -> truck.getParcels().stream())
                .map(PlacedParcel::getParcel)
                .toList();
    }

    /**
     * Возвращает весь доступный список посылок.
     *
     * @return список посылок
     */
    public List<Parcel> findAllParcels() {
        return parcelsRepository.findAllParcels();
    }

    /**
     * Находит посылку по наименованию посылки
     *
     * @param name наименование посылки
     * @return объект посылки
     */
    public Parcel findParcel(String name) {
        return parcelsRepository.findParcel(name)
                .orElseThrow(() -> new BusinessException("Посылка не найдена: " + name));
    }

    /**
     * Добавляет посылку
     *
     * @param parcel объект посылки
     */
    public void addParcel(Parcel parcel) {
        if (parcelsRepository.findParcel(parcel.getName()).isPresent()) {
            throw new BusinessException("Такая посылка уже есть: " + parcel.getName());
        }

        parcelsRepository.addParcel(parcel);
    }

    /**
     * Редактирует посылку
     *
     * @param parcel объект посылки
     */
    public void updateParcel(Parcel parcel) {
        parcelsRepository.findParcel(parcel.getName())
                .ifPresentOrElse(x -> {
                    parcelsRepository.updateParcel(parcel);
                }, () -> {
                    throw new BusinessException("Посылка не найдена: " + parcel.getName());
                });
    }

    /**
     * Удаляет посылку
     *
     * @param parcelName наименование посылки
     */
    public void deleteParcel(String parcelName) {
        parcelsRepository.findParcel(parcelName)
                .ifPresentOrElse(x -> {
                    parcelsRepository.deleteParcel(parcelName);
                }, () -> {
                    throw new BusinessException("Посылка не найдена: " + parcelName);
                });
    }

    /**
     * Возвращает посылки по наименованиям посылок
     *
     * @param parcelNames список с наименованиями посылок
     * @return список посылок
     */
    private List<Parcel> findParcels(List<String> parcelNames) {
        List<Parcel> parcels = new ArrayList<>();
        List<String> notFoundParcels = new ArrayList<>();
        for (var parcelName : parcelNames) {
            parcelsRepository.findParcel(parcelName).ifPresentOrElse(
                    parcels::add,
                    () -> notFoundParcels.add(parcelName));
        }

        if (notFoundParcels.isEmpty()) {
            return parcels;
        }

        throw new BusinessException("Посылки не найдены: \n" + String.join("\n", notFoundParcels));
    }

    private List<String> readParcelNamesFromFileIfEmpty(List<String> parcelNames, String fileName) {
        return parcelNames == null || parcelNames.isEmpty()
                ? fileReader.readAllLines(fileName)
                : parcelNames;
    }

    private void validateParcels(List<Parcel> parcels) {
        var errors = parcels.stream().flatMap(x -> parcelValidator.validate(x).stream()).toList();
        if (!errors.isEmpty()) {
            throw new ParcelValidatorException("Не валидная посылка: \n" + String.join("\n", errors));
        }
    }

}
