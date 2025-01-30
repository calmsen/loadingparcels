package ru.calmsen.loadingparcels.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.calmsen.loadingparcels.exception.BusinessException;
import ru.calmsen.loadingparcels.exception.ParcelValidatorException;
import ru.calmsen.loadingparcels.mapper.ParcelsMapperImpl;
import ru.calmsen.loadingparcels.model.domain.Parcel;
import ru.calmsen.loadingparcels.model.domain.PlacedParcel;
import ru.calmsen.loadingparcels.model.domain.Truck;
import ru.calmsen.loadingparcels.model.domain.enums.LoadingMode;
import ru.calmsen.loadingparcels.model.dto.ParcelDto;
import ru.calmsen.loadingparcels.repository.ParcelsRepository;
import ru.calmsen.loadingparcels.service.loadingalgorithm.LoadingAlgorithm;
import ru.calmsen.loadingparcels.service.loadingalgorithm.OneParcelLoadingAlgorithm;
import ru.calmsen.loadingparcels.service.parser.ParcelsParser;
import ru.calmsen.loadingparcels.service.parser.TrucksParser;
import ru.calmsen.loadingparcels.util.FileReader;
import ru.calmsen.loadingparcels.validator.ParcelValidator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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
    private Map<LoadingMode, LoadingAlgorithm> loadingAlgorithms;

    @Mock
    private FileReader fileReader;

    @Mock
    private BillingsService billingsService;

    private ParcelsService parcelsService;

    @BeforeEach
    void setUp() {
        parcelsService = new ParcelsService(
                new ParcelsMapperImpl(),
                parcelsParser,
                trucksParser,
                parcelValidator,
                loadingAlgorithms,
                parcelsRepository,
                fileReader,
                billingsService);
    }

    @Test
    public void initParcels_ValidFile_ParcelsAdded() {
        // Arrange
        String fileName = "testFile.txt";
        List<Parcel> parcels = List.of(new Parcel("Parcel1", "x", 's'), new Parcel("Parcel2", "x", 's'));
        when(parcelsParser.parseParcelsFromFile(fileName)).thenReturn(parcels);
        when(parcelValidator.validate(any(Parcel.class))).thenReturn(List.of());
        when(parcelsRepository.saveAll(parcels)).thenReturn(parcels);

        // Act
        parcelsService.initParcels(fileName);

        // Assert
        verify(parcelsRepository).saveAll(parcels);
    }

    @Test
    public void initParcels_InvalidParcels_ThrowsParcelValidatorException() {
        // Arrange
        String fileName = "testFile.txt";
        List<Parcel> parcels = List.of(new Parcel("Parcel1", "x", 's'), new Parcel("Parcel2", "x", 's'));
        when(parcelsParser.parseParcelsFromFile(fileName)).thenReturn(parcels);
        when(parcelValidator.validate(any(Parcel.class))).thenReturn(List.of("Error1", "Error2"));

        // Act
        Throwable thrown = catchThrowable(() -> parcelsService.initParcels(fileName));

        // Act and Assert
        assertThat(thrown).isInstanceOf(ParcelValidatorException.class)
                .hasMessage("Не валидная посылка: \nError1\nError2\nError1\nError2");
    }

    @Test
    public void loadParcels_ParcelNamesProvided_LoadsParcels() {
        // Arrange
        List<String> parcelNames = List.of("Parcel1", "Parcel2");
        List<Parcel> parcels = List.of(new Parcel("Parcel1", "x", 's'), new Parcel("Parcel2", "x", 's'));
        when(parcelsRepository.findById(anyString())).thenReturn(Optional.of(parcels.get(0)), Optional.of(parcels.get(1)));
        when(loadingAlgorithms.get(any())).thenReturn(new OneParcelLoadingAlgorithm());

        // Act
        parcelsService.loadParcels("user1", parcelNames, LoadingMode.ONEPARCEL, List.of(new Truck(6, 6), new Truck(6, 6)));

        // Assert
        verify(parcelsRepository).findById("Parcel1");
        verify(parcelsRepository).findById("Parcel2");
    }

    @Test
    public void loadParcels_ParcelNamesNotProvided_ReadsFromfile_LoadsParcels() {
        // Arrange
        String fileName = "testFile.txt";
        List<String> fileContent = List.of("Parcel1", "Parcel2");
        when(fileReader.readAllLines(fileName)).thenReturn(fileContent);
        when(parcelsRepository.findById(anyString())).thenReturn(Optional.of(new Parcel("Parcel1", "x", 's')), Optional.of(new Parcel("Parcel2", "x", 's')));
        when(loadingAlgorithms.get(any())).thenReturn(new OneParcelLoadingAlgorithm());

        // Act
        parcelsService.loadParcels("user1", fileName, LoadingMode.ONEPARCEL, List.of(new Truck(6, 6), new Truck(6, 6)));

        // Assert
        verify(fileReader).readAllLines(fileName);
    }

    @Test
    public void loadParcels_ParcelNamesInvalid_ThrowsBusinessException() {
        // Arrange
        List<String> parcelNames = List.of("Parcel1", "Parcel3");
        List<Parcel> parcels = List.of(new Parcel("Parcel1", "x", 's'));
        when(parcelsRepository.findById(anyString())).thenReturn(Optional.of(new Parcel("Parcel1", "x", 's')), Optional.empty());

        // Act
        Throwable thrown = catchThrowable(() -> parcelsService.loadParcels("user1", parcelNames, LoadingMode.ONEPARCEL, List.of(new Truck(6, 6))));

        // Act and Assert
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessage("Посылки не найдены: \nParcel3");
    }

    @Test
    public void unloadTrucks_ValidFile_ReturnsParcels() {
        // Arrange
        String fileName = "testFile.txt";
        var parcel = new Parcel("Parcel1", "x", 's');
        List<Truck> trucks = List.of(new Truck(6, 6, new PlacedParcel(parcel, 0, 0)));
        when(trucksParser.parseTrucksFromFile(fileName)).thenReturn(trucks);

        // Act
        List<Parcel> result = parcelsService.unloadTrucks("user1", fileName);

        // Assert
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.getFirst()).isEqualTo(parcel);
    }

    @Test
    public void findAllParcels_Success() {
        // Arrange
        List<Parcel> parcels = List.of(new Parcel("Parcel1", "x", 's'), new Parcel("Parcel2", "x", 's'));
        when(parcelsRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(parcels));

        // Act
        List<Parcel> result = parcelsService.findAllParcels(1, 1);

        // Assert
        assertThat(result).isEqualTo(parcels);
    }

    @Test
    public void findParcel_ParcelExists() {
        // Arrange
        Parcel parcel = new Parcel("Parcel2", "x", 's');
        when(parcelsRepository.findById(anyString())).thenReturn(Optional.of(parcel));

        // Act
        Parcel result = parcelsService.findParcel("Parcel1");

        // Assert
        assertThat(result).isEqualTo(parcel);
    }

    @Test
    public void findParcel_ParcelDoesNotExist() {
        // Arrange
        when(parcelsRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Throwable thrown = catchThrowable(() -> parcelsService.findParcel("Parcel1"));

        // Act and Assert
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessage("Посылка не найдена: Parcel1");
    }

    @Test
    public void addParcel_ParcelDoesNotExist() {
        // Arrange
        var parcel = new Parcel("Parcel1", "x", 's');
        var parcelDto = ParcelDto.builder()
                .name("Parcel1")
                .form("x")
                .symbol('s')
                .width(1)
                .height(1)
                .dimensions(1)
                .build();
        when(parcelsRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        parcelsService.addParcel(parcelDto);

        // Assert
        ArgumentCaptor<Parcel> captor = ArgumentCaptor.forClass(Parcel.class);
        verify(parcelsRepository).save(captor.capture());

        Parcel savedParcel = captor.getValue();
        assertThat(savedParcel)
                .usingRecursiveComparison().isEqualTo(parcel);
    }

    @Test
    public void addParcel_ParcelAlreadyExists() {
        // Arrange
        var parcel = new Parcel("Parcel1", "x", 's');
        var parcelDto = ParcelDto.builder()
                .name("Parcel1")
                .form("x")
                .symbol('s')
                .width(1)
                .height(1)
                .dimensions(1)
                .build();
        when(parcelsRepository.findById(anyString())).thenReturn(Optional.of(parcel));

        // Act
        Throwable thrown = catchThrowable(() -> parcelsService.addParcel(parcelDto));

        // Act and Assert
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessage("Такая посылка уже есть: " + parcel.getName());
    }

    @Test
    public void updateParcel_ParcelExists() {
        // Arrange
        var parcel = new Parcel("Parcel1", "x", 's');
        var parcelDto = ParcelDto.builder()
                .name("Parcel1")
                .form("x")
                .symbol('s')
                .width(1)
                .height(1)
                .dimensions(1)
                .build();
        when(parcelsRepository.findById(anyString())).thenReturn(Optional.of(parcel));

        // Act
        parcelsService.updateParcel(parcelDto);

        // Assert
        ArgumentCaptor<Parcel> captor = ArgumentCaptor.forClass(Parcel.class);
        verify(parcelsRepository).save(captor.capture());

        Parcel savedParcel = captor.getValue();
        assertThat(savedParcel)
                .usingRecursiveComparison().isEqualTo(parcel);
    }

    @Test
    public void updateParcel_ParcelDoesNotExist() {
        // Arrange
        var parcelDto = ParcelDto.builder()
                .name("Parcel1")
                .form("x")
                .symbol('s')
                .width(1)
                .height(1)
                .dimensions(1)
                .build();
        when(parcelsRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Throwable thrown = catchThrowable(() -> parcelsService.updateParcel(parcelDto));

        // Act and Assert
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessage("Посылка не найдена: Parcel1");
    }

    @Test
    public void deleteParcel_ParcelExists() {
        // Arrange
        when(parcelsRepository.findById(anyString())).thenReturn(Optional.of(new Parcel("Parcel1", "x", 's')));

        // Act
        parcelsService.deleteParcel("Parcel1");

        // Assert
        verify(parcelsRepository).deleteById("Parcel1");
    }

    @Test
    public void deleteParcel_ParcelDoesNotExist() {
        // Arrange
        when(parcelsRepository.findById(anyString())).thenReturn(Optional.empty());

        // Act
        Throwable thrown = catchThrowable(() -> parcelsService.deleteParcel("Parcel1"));

        // Act and Assert
        assertThat(thrown).isInstanceOf(BusinessException.class)
                .hasMessage("Посылка не найдена: Parcel1");
    }
}
