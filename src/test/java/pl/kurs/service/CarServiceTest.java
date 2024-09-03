package pl.kurs.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.repository.CarRepository;
import java.util.Arrays;
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
        assertEquals("BMW", result.getContent().get(1).getBrand());
        verify(carRepository, times(1)).findAll(pageable);
    }

    @Test
    public void shouldAddCar() {
        Car car = new Car("BMW", "M5", "diesel");
        CreateCarCommand command = new CreateCarCommand("BMW", "M5", "diesel");
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
        Car result = carService.addCar(command);
        assertEquals("BMW", result.getBrand());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    public void shouldFindCarById() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car1));
        Car result = carService.findCarById(1);
        assertEquals("Audi", result.getBrand());
        verify(carRepository, times(1)).findById(anyInt());
    }

    @Test
    public void shouldThrowExceptionWhenCarNotFound() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> carService.findCarById(999));
        verify(carRepository, times(1)).findById(anyInt());
    }
//    @Test(expected = CarNotFoundException.class)
//    public void shouldThrowExceptionWhenCarNotFound() {
//        when(carRepository.findById(anyInt())).thenReturn(Optional.empty());
//        carService.findCarById(999);
//    }

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
        Car result = carService.editCar(1, command);
        assertNotNull(result);
        assertEquals("BMW", result.getBrand());
        assertEquals("M6", result.getModel());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    public void shouldEditCarPartially() {
        Car car = new Car("Mercedes", "C63", "petrol");
        CreateCarCommand command = new CreateCarCommand(null, "M6", null);
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
        Car result = carService.editCarPartially(1, command);
        assertEquals("Mercedes", result.getBrand());
        assertEquals("M6", result.getModel());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }
}

