package pl.kurs.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.repository.CarRepository;
import java.util.Arrays;
=======
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.repository.CarRepository;
import java.util.List;
>>>>>>> origin/master
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

<<<<<<< HEAD
class CarServiceTest {
=======
public class CarServiceTest {
>>>>>>> origin/master
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

<<<<<<< HEAD
    @Test
    public void shouldReturnAllCars() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Car> page = new PageImpl<>(Arrays.asList(car1, car2), pageable, 2);
        when(carRepository.findAll(pageable)).thenReturn(page);
        Page<Car> result = carService.findAllCars(pageable);
        assertEquals(2, result.getTotalElements());
        verify(carRepository, times(1)).findAll(pageable);
=======
//    @Test
//    public void shouldInitializeData() {
//        carService.init();
//    }


    @Test
    public void shouldReturnAllCars() {
        when(carRepository.findAll()).thenReturn(List.of(car1, car2));
        List<CarDto> cars = carService.findAllCars();
        assertEquals(2, cars.size());
        verify(carRepository, times(1)).findAll();
>>>>>>> origin/master
    }

    @Test
    public void shouldAddCar() {
        CreateCarCommand command = new CreateCarCommand("Audi", "RS5", "petrol");
        Car car = new Car(command.getBrand(), command.getModel(), command.getFuelType());
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
<<<<<<< HEAD
        Car created = carService.addCar(command);
        assertEquals("Audi", created.getBrand());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }
    @Test
    public void shouldFindCarById() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car1));
        Car created = carService.findCarById(1);
        assertEquals("Audi", created.getBrand());
        verify(carRepository, times(1)).findById(anyInt());
    }
=======
        CarDto carDto = carService.addCar(command);
        assertEquals("Audi", carDto.brand());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

    @Test
    public void shouldFindCarById() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car1));
        CarDto carDto = carService.findCarById(1);
        assertEquals("Audi", carDto.brand());
        verify(carRepository, times(1)).findById(anyInt());
    }

>>>>>>> origin/master
    @Test
    public void shouldThrowExceptionWhenCarNotFound() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(CarNotFoundException.class, () -> carService.findCarById(999));
        verify(carRepository, times(1)).findById(anyInt());
    }
<<<<<<< HEAD
    //    @Test(expected = CarNotFoundException.class)
=======
//    @Test(expected = CarNotFoundException.class)
>>>>>>> origin/master
//    public void shouldThrowExceptionWhenCarNotFound() {
//        when(carRepository.findById(anyInt())).thenReturn(Optional.empty());
//        carService.findCarById(999);
//    }
<<<<<<< HEAD
=======

>>>>>>> origin/master
    @Test
    public void shouldDeleteCarById() {
        carService.deleteCarById(1);
        verify(carRepository, times(1)).deleteById(anyInt());
    }
<<<<<<< HEAD
=======

>>>>>>> origin/master
    @Test
    public void shouldEditCar() {
        CreateCarCommand command = new CreateCarCommand("BMW", "M6", "petrol");
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car1));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car1);
<<<<<<< HEAD
        Car created = carService.editCar(1, command);
        assertNotNull(created);
        assertEquals("BMW", created.getBrand());
        assertEquals("M6", created.getModel());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }
=======
        CarDto carDto = carService.editCar(1, command);
        assertNotNull(carDto);
        assertEquals("BMW", carDto.brand());
        assertEquals("M6", carDto.model());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }

>>>>>>> origin/master
    @Test
    public void shouldEditCarPartially() {
        Car car = new Car("Mercedes", "C63", "petrol");
        CreateCarCommand command = new CreateCarCommand(null, "M6", null);
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
<<<<<<< HEAD
        Car created = carService.editCarPartially(1, command);
        assertEquals("Mercedes", created.getBrand());
        assertEquals("M6", created.getModel());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }
}
=======
        CarDto carDto = carService.editCarPartially(1, command);
        assertEquals("Mercedes", carDto.brand());
        assertEquals("M6", carDto.model());
        verify(carRepository, times(1)).findById(anyInt());
        verify(carRepository, times(1)).saveAndFlush(any(Car.class));
    }
}

>>>>>>> origin/master
