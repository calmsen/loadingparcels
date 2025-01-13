package ru.calmsen.loadingparcels.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.exception.ParcelValidatorException;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.PlacedParcel;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;
import ru.calmsen.loadingparcels.repository.ParcelsRepository;
import ru.calmsen.loadingparcels.service.loadingalgorithm.LoadingAlgorithmFactory;
import ru.calmsen.loadingparcels.service.loadingalgorithm.OneParcelLoadingAlgorithm;
import ru.calmsen.loadingparcels.service.parser.ParcelsParser;
import ru.calmsen.loadingparcels.service.parser.TrucksParser;
import ru.calmsen.loadingparcels.util.FileReader;
import ru.calmsen.loadingparcels.validator.ParcelValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParcelsServiceTest {

    @Mock
    private ParcelsRepository parcelsRepository;

    @Mock
    private ParcelsParser parcelsParser;

    @Mock
    private TrucksParser trucksParser;

    @Mock
    private ParcelValidator parcelValidator;

    @Mock
    private LoadingAlgorithmFactory loadingAlgorithmFactory;

    @Mock
    private FileReader fileReader;

    @InjectMocks
    private ParcelsService parcelsService;

    @Test
    public void initParcels_ValidFile_ParcelsAdded() {
        // Arrange
        String fileName = "testFile.txt";
        List<Parcel> parcels = List.of(new Parcel("Parcel1", "x", 's'), new Parcel("Parcel2", "x", 's'));
        when(parcelsParser.parseParcelsFromFile(fileName)).thenReturn(parcels);
        when(parcelValidator.validate(any(Parcel.class))).thenReturn(List.of());

        // Act
        parcelsService.initParcels(fileName);

        // Assert
        // No exception should be thrown, and parcels should be added to the repository
    }

    @Test
    public void initParcels_InvalidParcels_ThrowsParcelValidatorException() {
        // Arrange
        String fileName = "testFile.txt";
        List<Parcel> parcels = List.of(new Parcel("Parcel1", "x", 's'), new Parcel("Parcel2", "x", 's'));
        when(parcelsParser.parseParcelsFromFile(fileName)).thenReturn(parcels);
        when(parcelValidator.validate(any(Parcel.class))).thenReturn(List.of("Error1", "Error2"));

        // Act and Assert
        var exception = assertThrows(ParcelValidatorException.class, () -> parcelsService.initParcels(fileName));
        assertEquals("Не валидная посылка: \n\nError1\nError2\nError1\nError2", exception.getMessage());
    }

    @Test
    public void loadParcels_ParcelNamesProvided_LoadsParcels() {
        // Arrange
        List<String> parcelNames = List.of("Parcel1", "Parcel2");
        when(parcelsRepository.findParcel(anyString())).thenReturn(Optional.of(new Parcel("Parcel1", "x", 's')), Optional.of(new Parcel("Parcel2", "x", 's')));
        when(loadingAlgorithmFactory.Create(any())).thenReturn(new OneParcelLoadingAlgorithm());

        // Act
        parcelsService.loadParcels(parcelNames, null, LoadingMode.ONEPARCEL, List.of(new Truck(6, 6), new Truck(6, 6)));

        // Assert
        // No exception should be thrown, and loading algorithm should be called
    }

    @Test
    public void loadParcels_ParcelNamesNotProvided_ReadsFromfile_LoadsParcels() {
        // Arrange
        List<String> parcelNames = List.of();
        String fileName = "testFile.txt";
        List<String> fileContent = List.of("Parcel1", "Parcel2");
        when(fileReader.readAllLines(fileName)).thenReturn(fileContent);
        when(parcelsRepository.findParcel(anyString())).thenReturn(Optional.of(new Parcel("Parcel1", "x", 's')), Optional.of(new Parcel("Parcel2", "x", 's')));
        when(loadingAlgorithmFactory.Create(any())).thenReturn(new OneParcelLoadingAlgorithm());

        // Act
        parcelsService.loadParcels(parcelNames, fileName, LoadingMode.ONEPARCEL, List.of(new Truck(6, 6), new Truck(6, 6)));

        // Assert
        // No exception should be thrown, and loading algorithm should be called
    }

    @Test
    public void loadParcels_ParcelNamesInvalid_ThrowsBusinessException() {
        // Arrange
        List<String> parcelNames = List.of("Parcel1", "Parcel3");
        List<Parcel> parcels = List.of(new Parcel("Parcel1", "x", 's'));
        when(parcelsRepository.findParcel(anyString())).thenReturn(Optional.of(new Parcel("Parcel1", "x", 's')), Optional.empty());

        // Act and Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> parcelsService.loadParcels(parcelNames, null, LoadingMode.ONEPARCEL, List.of(new Truck(6, 6))));
        assertEquals("Посылки не найдены: \n\nParcel3", exception.getMessage());
    }

    @Test
    public void unloadTrucks_ValidFile_ReturnsParcels() {
        // Arrange
        String fileName = "testFile.txt";
        var parcel = new Parcel("Parcel1", "x", 's');
        List<Truck> trucks = List.of(new Truck(6 ,6, new PlacedParcel(parcel, 0, 0)));
        when(trucksParser.parseTrucksFromFile(fileName)).thenReturn(trucks);

        // Act
        List<Parcel> result = parcelsService.unloadTrucks(fileName);

        // Assert
        assertEquals(1, result.size());
        assertEquals(parcel, result.getFirst());
    }

    @Test
    public void findAllParcels_Success() {
        // Arrange
        List<Parcel> parcels = List.of(new Parcel("Parcel1", "x", 's'), new Parcel("Parcel2", "x", 's'));
        when(parcelsRepository.findAllParcels()).thenReturn(parcels);

        // Act
        List<Parcel> result = parcelsService.findAllParcels();

        // Assert
        assertEquals(parcels, result);
    }

    @Test
    public void findParcel_ParcelExists() {
        // Arrange
        Parcel parcel = new Parcel("Parcel1", "x", 's');
        when(parcelsRepository.findParcel(anyString())).thenReturn(Optional.of(parcel));

        // Act
        Parcel result = parcelsService.findParcel("Parcel1");

        // Assert
        assertEquals(parcel, result);
    }

    @Test
    public void findParcel_ParcelDoesNotExist() {
        // Arrange
        when(parcelsRepository.findParcel(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> parcelsService.findParcel("Parcel1"));
        assertEquals("Посылка не найдена: Parcel1", exception.getMessage());
    }

    @Test
    public void addParcel_ParcelDoesNotExist() {
        // Arrange
        Parcel parcel = new Parcel("Parcel1", "x", 's');
        when(parcelsRepository.findParcel(anyString())).thenReturn(Optional.empty());

        // Act
        parcelsService.addParcel(parcel);

        // Assert (No exception should be thrown)
    }

    @Test
    public void addParcel_ParcelAlreadyExists() {
        // Arrange
        Parcel parcel = new Parcel("Parcel1", "x", 's');
        when(parcelsRepository.findParcel(anyString())).thenReturn(Optional.of(parcel));

        // Act and Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> parcelsService.addParcel(parcel));
        assertEquals("Такая посылка уже есть", exception.getMessage());
    }

    @Test
    public void updateParcel_ParcelExists() {
        // Arrange
        Parcel parcel = new Parcel("Parcel1", "x", 's');
        when(parcelsRepository.findParcel(anyString())).thenReturn(Optional.of(parcel));

        // Act
        parcelsService.updateParcel(parcel);

        // Assert (No exception should be thrown)
    }

    @Test
    public void updateParcel_ParcelDoesNotExist() {
        // Arrange
        Parcel parcel = new Parcel("Parcel1", "x", 's');
        when(parcelsRepository.findParcel(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> parcelsService.updateParcel(parcel));
        assertEquals("Посылка не найдена: Parcel1", exception.getMessage());
    }

    @Test
    public void deleteParcel_ParcelExists() {
        // Arrange
        when(parcelsRepository.findParcel(anyString())).thenReturn(Optional.of(new Parcel("Parcel1", "x", 's')));

        // Act
        parcelsService.deleteParcel("Parcel1");

        // Assert (No exception should be thrown)
    }

    @Test
    public void deleteParcel_ParcelDoesNotExist() {
        // Arrange
        when(parcelsRepository.findParcel(anyString())).thenReturn(Optional.empty());

        // Act and Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> parcelsService.deleteParcel("Parcel1"));
        assertEquals("Посылка не найдена: Parcel1", exception.getMessage());
    }
}