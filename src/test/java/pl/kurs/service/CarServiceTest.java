package pl.kurs.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.repository.CarRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void shouldInitializeData() {
//        carService.init();
//    }


    @Test
    public void shouldReturnAllCars() {
        Car car1 = new Car("Audi", "RS5", "petrol");
        Car car2 = new Car("BMW", "M3", "diesel");
        when(carRepository.findAll()).thenReturn(List.of(car1, car2));
        List<CarDto> cars = carService.findAllCars();
        assertEquals(2, cars.size());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    public void shouldAddCar() {
        CreateCarCommand command = new CreateCarCommand("Audi", "RS5", "petrol");
        Car car = new Car(command.getBrand(), command.getModel(), command.getFuelType());
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
        CarDto carDto = carService.addCar(command);
        assertEquals("Audi", carDto.brand());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    public void shouldFindCarById() {
        Car car = new Car("Audi", "RS5", "petrol");
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        CarDto carDto = carService.findCarById(1);
        assertEquals("Audi", carDto.brand());
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
        Car car = new Car("Mercedes", "C63", "petrol");
        CreateCarCommand command = new CreateCarCommand("BMW", "M6", "petrol");
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
        CarDto carDto = carService.editCar(1, command);
        assertNotNull(carDto);
        assertEquals("BMW", carDto.brand());
        assertEquals("M6", carDto.model());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    public void shouldEditCarPartially() {
        Car car = new Car("Mercedes", "C63", "petrol");
        CreateCarCommand command = new CreateCarCommand(null, "M6", null);
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
        CarDto carDto = carService.editCarPartially(1, command);
        assertEquals("Mercedes", carDto.brand());
        assertEquals("M6", carDto.model());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }
}

