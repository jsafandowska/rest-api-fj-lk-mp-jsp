package pl.kurs.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.model.dto.GarageDto;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GarageServiceTest {
    @Mock
    private GarageRepository garageRepository;
    @Mock
    private CarRepository carRepository;
    @InjectMocks
    private GarageService garageService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAllGarages() {
        Garage garage1 = new Garage(2, "X", true);
        Garage garage2 = new Garage(1, "Y", true);
        when(garageRepository.findAll()).thenReturn(List.of(garage1, garage2));
        List<GarageDto> garages = garageService.findAllGarages();
        assertEquals(2, garages.size());
        verify(garageRepository, times(1)).findAll();
    }

    @Test
    void shouldAddGarage() {
        CreateGarageCommand command = new CreateGarageCommand(1, "A", true);
        Garage garage = new Garage(command.getPlaces(), command.getAddress(), command.isLpgAllowed());
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage);
        GarageDto dto = garageService.addGarage(command);
        assertEquals("A", dto.address());
        verify(garageRepository, times(1)).saveAndFlush(any(Garage.class));

    }

    @Test
    void shouldFindGarage() {
        Garage garage = new Garage(1, "B", true);
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage));
        GarageDto dto = garageService.findGarage(garage.getId());
        assertEquals("B", dto.address());
        verify(garageRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testDeleteGarage() {
        doNothing().when(garageRepository).deleteById(1);
        garageService.deleteGarage(1);
        verify(garageRepository, times(1)).deleteById(1);
    }

    @Test
    void shouldEditGarage() {
        Garage garage = new Garage(2, "Warszawa", true);
        CreateGarageCommand command = new CreateGarageCommand(3, "Poznań", false);
        when(garageRepository.findById(1)).thenReturn(Optional.of(garage));
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage);
        GarageDto result = garageService.editGarage(1, command);
        assertEquals("Poznań", result.address());
        assertEquals(3, result.places());
        verify(garageRepository, times(1)).findById(1);
        verify(garageRepository, times(1)).saveAndFlush(any(Garage.class));
    }


    @Test
    void shouldEditGaragePartially() {
        Garage garage = new Garage(2, "Warszawa", true);
        EditGarageCommand command = new EditGarageCommand(null, "Poznań", null);
        when(garageRepository.findById(1)).thenReturn(Optional.of(garage));
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage);
        GarageDto result = garageService.editGaragePartially(1, command);
        assertEquals("Poznań", result.address());
        assertEquals(2, result.places());
        verify(garageRepository, times(1)).findById(1);
        verify(garageRepository, times(1)).saveAndFlush(any(Garage.class));
    }


    @Test
    public void testAddCarToGarage() {
        Garage garage = new Garage(2, "Warszawa", true);
        Car car = new Car("Mercedes", "S-class", "petrol");
        when(garageRepository.findById(1)).thenReturn(Optional.of(garage));
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage);
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
        GarageDto result = garageService.addCarToGarage(1, 1);
        assertEquals(result.address(), "Warszawa");
        assertEquals(result.places(), 2);
        assertEquals(result.lpgAllowed(),true);
        assertEquals(car.getGarage(),garage);
        verify(garageRepository, times(1)).findById(1);
        verify(garageRepository, times(1)).saveAndFlush(any(Garage.class));
    }

    @Test
    public void shouldDeleteCarFromGarage() {
        Garage garage = new Garage(2, "Warszawa", true);
        Car car = new Car("Mercedes", "S-class", "petrol");
        garage.addCar(car);
        when(garageRepository.findById(1)).thenReturn(Optional.of(garage));
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        garageService.deleteCarFromGarage(1, 1);
        assertFalse(garage.getCars().contains(car));
        verify(garageRepository, times(1)).findById(1);
        verify(carRepository, times(1)).findById(1);

    }
}