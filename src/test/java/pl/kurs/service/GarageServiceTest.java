package pl.kurs.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.model.dto.GarageDto;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;

import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class GarageServiceTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private GarageService garageService;

    private Garage garage;
    private Car car;

    @BeforeEach
    public void setUp() {
        garage = new Garage(2, "Test Address", true);
        garage.setId(1);

        car = new Car("Audi", "A3", "LPG");
        car.setId(1);
        car.setGarage(garage);

        garage.getCars().add(car);
    }

    @Test
    public void testFindAll() {
        when(garageRepository.findAll()).thenReturn(List.of(garage));

        List<GarageDto> result = garageService.findAll();

        assertEquals(1, result.size());
        verify(garageRepository, times(1)).findAll();
    }

    @Test
    public void testAddGarage() {
        CreateGarageCommand command = new CreateGarageCommand(2, "New Address", true);
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage);

        GarageDto result = garageService.addGarage(command);

        assertEquals(garage.getId(), result.id());
        verify(garageRepository, times(1)).saveAndFlush(any(Garage.class));
    }

    @Test
    public void testFindGarage() {
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage));

        GarageDto result = garageService.findGarage(1);

        assertEquals(garage.getId(), result.id());
        verify(garageRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testDeleteGarage() {
        doNothing().when(garageRepository).deleteById(anyInt());

        garageService.deleteGarage(1);

        verify(garageRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void testEditGarage() {
        CreateGarageCommand command = new CreateGarageCommand(3, "Updated Address", false);
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage));
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage);

        GarageDto result = garageService.editGarage(1, command);

        assertEquals(3, result.places());
        assertEquals("Updated Address", result.address());
        assertFalse(result.lpgAllowed());
        verify(garageRepository, times(1)).findById(anyInt());
        verify(garageRepository, times(1)).saveAndFlush(any(Garage.class));
    }

    @Test
    public void testEditGaragePartially() {
        EditGarageCommand command = new EditGarageCommand(null, "Updated Address", null);
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage));
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage);

        GarageDto result = garageService.editGaragePartially(1, command);

        assertEquals("Updated Address", result.address());
        verify(garageRepository, times(1)).findById(anyInt());
        verify(garageRepository, times(1)).saveAndFlush(any(Garage.class));
    }

    @Test
    public void testAddCar() {
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage));
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);

        garageService.addCar(1, 1);

        assertEquals(garage, car.getGarage());
        verify(garageRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    public void testDeleteCarFromGarage() {
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage));
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);

        garageService.deleteCarFromGarage(1, 1);

        assertNull(car.getGarage());
        verify(garageRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }
}
