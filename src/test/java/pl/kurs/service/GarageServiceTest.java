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
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.model.dto.GarageDto;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;

import java.util.Arrays;
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
    private Garage garage1;
    private Garage garage2;
    private Car car;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        garage1 = new Garage(2, "X", true);
        garage2 = new Garage(1, "Y", true);
        car = new Car("Mercedes", "S-class", "petrol");
    }

    @Test
    void shouldReturnAllGarages() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Garage> page = new PageImpl<>(Arrays.asList(garage1, garage2), pageable, 2);
        when(garageRepository.findAll(pageable)).thenReturn(page);
        Page<Garage> garages = garageService.findAllGarages(pageable);
        assertEquals(2, garages.getTotalElements());
        verify(garageRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldAddGarage() {
        CreateGarageCommand command = new CreateGarageCommand(1, "A", true);
        Garage garage = new Garage(command.getPlaces(), command.getAddress(), command.isLpgAllowed());
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage);
        Garage added = garageService.addGarage(command);
        assertEquals("A", added.getAddress());
        verify(garageRepository, times(1)).saveAndFlush(any(Garage.class));

    }

    @Test
    void shouldFindGarage() {
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage1));
        Garage result = garageService.findGarage(garage1.getId());
        assertEquals("X", result.getAddress());
        verify(garageRepository, times(1)).findById(anyInt());
    }

    @Test
    public void shouldDeleteGarage() {
        doNothing().when(garageRepository).deleteById(1);
        garageService.deleteGarage(1);
        verify(garageRepository, times(1)).deleteById(1);
    }

    @Test
    void shouldEditGarage() {
        CreateGarageCommand command = new CreateGarageCommand(3, "Poznań", false);
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage1));
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage1);
        Garage result = garageService.editGarage(1, command);
        assertEquals("Poznań", result.getAddress());
        assertEquals(3, result.getPlaces());
        verify(garageRepository, times(1)).findById(1);
        verify(garageRepository, times(1)).saveAndFlush(any(Garage.class));
    }


    @Test
    void shouldEditGaragePartially() {
        EditGarageCommand command = new EditGarageCommand(null, "Poznań", null);
        when(garageRepository.findById(1)).thenReturn(Optional.of(garage1));
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage1);
        Garage result = garageService.editGaragePartially(1, command);
        assertEquals("Poznań", result.getAddress());
        assertEquals(2, result.getPlaces());
        verify(garageRepository, times(1)).findById(1);
        verify(garageRepository, times(1)).saveAndFlush(any(Garage.class));
    }


    @Test
    public void shouldAddCarToGarage() {
        when(garageRepository.findById(1)).thenReturn(Optional.of(garage1));
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage1);
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car);
        garageService.addCarToGarage(1, 1);
        verify(garageRepository, times(1)).findById(1);
    }

    @Test
    public void shouldDeleteCarFromGarage() {
        garage1.addCar(car);
        when(garageRepository.findById(1)).thenReturn(Optional.of(garage1));
        when(carRepository.findById(1)).thenReturn(Optional.of(car));
        garageService.deleteCarFromGarage(1, 1);
        assertFalse(garage1.getCars().contains(car));
        verify(garageRepository, times(1)).findById(1);
        verify(carRepository, times(1)).findById(1);

    }
}