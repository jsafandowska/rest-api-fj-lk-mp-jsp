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
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.command.EditCarCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.model.dto.GarageDto;
import pl.kurs.repository.CarRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CarServiceTest {
    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;
    private Car car1;
    private Car car2;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        car1 = new Car("Audi", "RS5", "petrol");
        car2 = new Car("Mercedes", "S-class", "petrol");
    }

    @Test
    public void shouldReturnAllCars() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Car> page = new PageImpl<>(Arrays.asList(car1, car2), pageable, 2);
        when(carRepository.findAll(pageable)).thenReturn(page);
        Page<CarDto> result = carService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(car1.getId(), result.getContent().get(0).id());
        assertEquals(car2.getId(), result.getContent().get(1).id());
    }

    @Test
    public void shouldAddCar() {
        CreateCarCommand createCarCommand = new CreateCarCommand("BMW", "M3", "petrol");
        Car car = new Car(createCarCommand.getBrand(), createCarCommand.getModel(), createCarCommand.getFuelType());
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
        CarDto carDto = carService.addCar(createCarCommand);
        assertEquals("BMW", carDto.brand());
        assertEquals("M3", carDto.model());
        assertEquals("petrol", carDto.fuelType());
    }

    @Test
    public void shouldFindCar() {
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car1));
        CarDto carDto = carService.findCar(car1.getId());
        assertEquals("Audi", carDto.brand());
        assertEquals("RS5", carDto.model());
        assertEquals("petrol", carDto.fuelType());
    }

    @Test
    public void shouldDeleteCar() {
        when(carRepository.findAll()).thenReturn(Arrays.asList(car1, car2));
        carService.deleteCar(1);
        verify(carRepository).deleteById(1);
    }

    @Test
    public void shouldEditCar() {
        CreateCarCommand createCarCommand = new CreateCarCommand("BMW", "M3", "petrol");
        when(carRepository.findById(anyInt())).thenReturn(Optional.of(car1));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car1);
        CarDto carDto = carService.editCar(1, createCarCommand);
        assertEquals("BMW", carDto.brand());
        assertEquals("M3", carDto.model());
        assertEquals("petrol", carDto.fuelType());
    }

    @Test
    public void shouldEditCarPartially() {
        CreateCarCommand createCarCommand = new CreateCarCommand(null, "RS6", null);
        when(carRepository.findById(1)).thenReturn(Optional.of(car1));
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car1);
        CarDto carDto = carService.editCarPartially(1, createCarCommand);
        assertEquals("Audi", carDto.brand());
        assertEquals("RS6", carDto.model());
        assertEquals("petrol", carDto.fuelType());
    }

}