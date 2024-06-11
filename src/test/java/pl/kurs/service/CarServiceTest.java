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
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.command.EditCarCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;

import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private GarageRepository garageRepository;

    @InjectMocks
    private CarService carService;

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
        when(carRepository.findAll()).thenReturn(List.of(car));

        List<CarDto> result = carService.findAll();

        assertEquals(1, result.size());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    public void testAddCar() {
        CreateCarCommand command = new CreateCarCommand("Audi", "A3", "LPG", garage.getId());
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);

        CarDto result = carService.addCar(command);

        assertEquals(car.getId(), result.id());
        verify(garageRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    public void testFindCar() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));

        CarDto result = carService.findCar(1);

        assertEquals(car.getId(), result.id());
        verify(carRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testDeleteCar() {
        doNothing().when(carRepository).deleteById(anyInt());

        carService.deleteCar(1);

        verify(carRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void testEditCar() {
        EditCarCommand command = new EditCarCommand("Updated Brand", "Updated Model", "Updated FuelType");
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);

        CarDto result = carService.editCar(1, command);

        assertEquals("Updated Brand", result.brand());
        assertEquals("Updated Model", result.model());
        assertEquals("Updated FuelType", result.fuelType());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    public void testEditCarPartially() {
        EditCarCommand command = new EditCarCommand(null, "Updated Model", null);
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);

        CarDto result = carService.editCarPartially(1, command);

        assertEquals(car.getBrand(), result.brand());
        assertEquals("Updated Model", result.model());
        assertEquals(car.getFuelType(), result.fuelType());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }
}
