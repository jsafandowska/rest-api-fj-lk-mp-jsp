package pl.kurs.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.repository.CarRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;
    private Car car1;
    private Car car2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        car1 = new Car("Audi", "RS5", "petrol");
        car2 = new Car("BMW", "M3", "diesel");
    }

//    @Test
//    public void shouldInitializeData() {
//        carService.init();
//    }


    @Test
    public void shouldReturnAllCars() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Car> page = new PageImpl<>(Arrays.asList(car1, car2), pageable, 2);
        when(carRepository.findAll(pageable)).thenReturn(page);
        Page<Car> result = carService.findAllCars(pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals("Audi", result.getContent().get(0).getBrand());
        verify(carRepository, times(1)).findAll(pageable);
    }

    @Test
    public void shouldAddCar() {
        CreateCarCommand command = new CreateCarCommand("Audi", "RS5", "petrol");
        Car car = new Car(command.getBrand(), command.getModel(), command.getFuelType());
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
        Car car2 = carService.addCar(command);
        assertEquals("Audi", car2.getBrand());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    public void shouldFindCarById() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car1));
        Car car = carService.findCarById(1);
        assertEquals("Audi", car.getBrand());
        verify(carRepository, times(1)).findById(anyInt());
    }

    @Test
    public void shouldThrowExceptionWhenCarNotFound() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> carService.findCarById(999));
        verify(carRepository, times(1)).findById(anyInt());
    }

    @Test
    public void shouldDeleteCarById() {
        carService.deleteCarById(1);
        verify(carRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void shouldEditCar() {
        CreateCarCommand command = new CreateCarCommand("BMW", "M6", "petrol");
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car1));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car1);
        Car car = carService.editCar(1, command);
        assertNotNull(car);
        assertEquals("BMW", car.getBrand());
        assertEquals("M6", car.getModel());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    public void shouldEditCarPartially() {
        Car car = new Car("Mercedes", "C63", "petrol");
        CreateCarCommand command = new CreateCarCommand(null, "M6", null);
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
        Car car2 = carService.editCarPartially(1, command);
        assertEquals("Mercedes", car2.getBrand());
        assertEquals("M6", car2.getModel());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }
}

