package ru.calmsen.loadingparcels.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.exception.ParcelValidatorException;
import ru.calmsen.loadingparcels.mapper.ParcelsMapper;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.PlacedParcel;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;
import ru.calmsen.loadingparcels.model.dto.ParcelDto;
import ru.calmsen.loadingparcels.repository.ParcelsRepository;
import ru.calmsen.loadingparcels.service.loadingalgorithm.LoadingAlgorithm;
import ru.calmsen.loadingparcels.service.parser.ParcelsParser;
import ru.calmsen.loadingparcels.service.parser.TrucksParser;
import ru.calmsen.loadingparcels.util.FileReader;
import ru.calmsen.loadingparcels.validator.ParcelValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с посылками
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ParcelsService {
    private final ParcelsMapper parcelsMapper;
    private final ParcelsParser parcelsParser;
    private final TrucksParser trucksParser;
    private final ParcelValidator parcelValidator;
    private final Map<LoadingMode, LoadingAlgorithm> loadingAlgorithms;
    private final ParcelsRepository parcelsRepository;
    private final FileReader fileReader;
    private final BillingsService billingsService;

    /**
     * Инициализирует доступный список посылок, расположенных в файле.
     *
     * @param fileName наименование файла.
     */
    @Transactional
    public void initParcels(String fileName) {
        var parcels = parcelsParser.parseParcelsFromFile(fileName);
        validateParcels(parcels);

        parcelsRepository.saveAll(parcels);
    }

    /**
     * Загружает посылки в машины. Наименования посылок можно передать в списке или в файле.
     *
     * @param user идентификатор пользователя
     * @param parcelNames список наименований посылок, разделенных \n
     * @param loadingMode тип погрузки
     * @param trucks      список машин
     */
    public void loadParcels(String user, List<String> parcelNames, LoadingMode loadingMode, List<Truck> trucks) {
        var parcels = findParcels(parcelNames);

        var loadingAlgorithm = loadingAlgorithms.get(loadingMode);
        loadingAlgorithm.loadParcels(parcels, trucks);
        billingsService.addParcelsBilling("Погрузка", user, trucks);
    }

    /**
     * Загружает посылки в машины. Наименования посылок можно передать в списке или в файле.
     *
     * @param user идентификатор пользователя
     * @param fileName    наименование файла со списком посылок.
     * @param loadingMode тип погрузки
     * @param trucks      список машин
     */
    @Transactional
    public void loadParcels(String user, String fileName, LoadingMode loadingMode, List<Truck> trucks) {
        var parcelNames = fileReader.readAllLines(fileName);
        var parcels = findParcels(parcelNames);

        var loadingAlgorithm = loadingAlgorithms.get(loadingMode);
        loadingAlgorithm.loadParcels(parcels, trucks);
        billingsService.addParcelsBilling("Погрузка", user, trucks);
    }

    /**
     * Разгружает посылки
     *
     * @param user идентификатор пользователя
     * @param fileName наименование файла со списком загруженных машин
     * @return список посылок
     */
    @Transactional
    public List<Parcel> unloadTrucks(String user, String fileName) {
        var trucks = trucksParser.parseTrucksFromFile(fileName);
        var parcels = trucks.stream()
                .flatMap(truck -> truck.getParcels().stream())
                .map(PlacedParcel::getParcel)
                .toList();
        billingsService.addParcelsBilling("Разгрузка", user, trucks);
        return parcels;
    }

    /**
     * Возвращает весь доступный список посылок.
     *
     * @param pageNumber номер страницы
     * @param pageSize кол-во посылок на странице
     * @return список посылок
     */
    public List<Parcel> findAllParcels(int pageNumber, int pageSize) {
        if (pageNumber <= 0) {
            pageNumber = 1;
        }

        if (pageSize <= 0) {
            pageSize = 1;
        }

        return parcelsRepository.findAll(PageRequest.of(pageNumber - 1, pageSize))
                .getContent();
    }

    /**
     * Находит посылку по наименованию посылки
     *
     * @param name наименование посылки
     * @return объект посылки
     */
    public Parcel findParcel(String name) {
        return parcelsRepository.findById(name)
                .orElseThrow(() -> new BusinessException("Посылка не найдена: " + name));
    }

    /**
     * Добавляет посылку
     *
     * @param parcel объект посылки
     */
    public void addParcel(ParcelDto parcel) {
        if (parcelsRepository.findById(parcel.getName()).isPresent()) {
            throw new BusinessException("Такая посылка уже есть: " + parcel.getName());
        }

        parcelsRepository.save(parcelsMapper.toParcel(parcel));
    }

    /**
     * Редактирует посылку
     *
     * @param parcel объект посылки
     */
    public void updateParcel(ParcelDto parcel) {
        parcelsRepository.findById(parcel.getName())
                .ifPresentOrElse(x -> {
                    parcelsRepository.save(parcelsMapper.toParcel(parcel));
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
        parcelsRepository.findById(parcelName)
                .ifPresentOrElse(x -> {
                    parcelsRepository.deleteById(parcelName);
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
            parcelsRepository.findById(parcelName).ifPresentOrElse(
                    parcels::add,
                    () -> notFoundParcels.add(parcelName));
        }

        if (notFoundParcels.isEmpty()) {
            return parcels;
        }

        throw new BusinessException("Посылки не найдены: \n" + String.join("\n", notFoundParcels));
    }

    private void validateParcels(List<Parcel> parcels) {
        var errors = parcels.stream().flatMap(x -> parcelValidator.validate(x).stream()).toList();
        if (!errors.isEmpty()) {
            throw new ParcelValidatorException("Не валидная посылка: \n" + String.join("\n", errors));
        }
    }

}
