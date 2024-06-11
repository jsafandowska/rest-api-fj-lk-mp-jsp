package pl.kurs.service;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class GarageServiceTest {
    @InjectMocks
    private GarageService garageService;
    @Mock
    private GarageRepository garageRepository;
    @Mock
    private CarRepository carRepository;
    private Garage garage1;
    private Garage garage2;
    private Car car1;
    private Car car2;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        garageService = new GarageService(garageRepository, carRepository);
        garage1 = new Garage(2, "Warszawa", true);
        garage2 = new Garage(3, "Piątkowska", false);
        car1 = new Car("Audi", "RS", "petrol");
        car2 = new Car("Mercedes", "S-class", "petrol");
        garageRepository.saveAndFlush(garage1);
        garageRepository.saveAndFlush(garage2);
        carRepository.saveAndFlush(car1);
        carRepository.saveAndFlush(car2);
    }

    @Test
    public void shouldReturnAllGarages() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Garage> page = new PageImpl<>(Arrays.asList(garage1, garage2), pageable, 2);
        when(garageRepository.findAll(pageable)).thenReturn(page);
        Page<GarageDto> result = garageService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(garage1.getId(), result.getContent().get(0).id());
        assertEquals(garage2.getId(), result.getContent().get(1).id());
    }

    @Test
    public void shouldAddGarage() {
        CreateGarageCommand createGarageCommand = new CreateGarageCommand(2, "Gdansk", true);
        Garage garage = new Garage(createGarageCommand.getPlaces(), createGarageCommand.getAddress(), createGarageCommand.isLpgAllowed());
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage);
        GarageDto garageDto = garageService.addGarage(createGarageCommand);
        assertEquals("Gdansk", garageDto.address());
        assertEquals(2, garageDto.places());
    }

    @Test
    public void shouldFindGarage() {
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage1));
        GarageDto garageDto = garageService.findGarage(garage1.getId());
        assertEquals("Warszawa", garageDto.address());
    }

    @Test
    public void shouldDeleteGarage() {
        when(garageRepository.findAll()).thenReturn(Arrays.asList(garage1, garage2));
        garageService.deleteGarage(1);
        verify(garageRepository).deleteById(1);
    }

    @Test
    public void shouldEditGarage() {
        CreateGarageCommand createGarageCommand = new CreateGarageCommand(2, "Sopot", true);
        when(garageRepository.findById(anyInt())).thenReturn(Optional.of(garage1));
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage1);
        GarageDto garageDto = garageService.editGarage(1, createGarageCommand);
        assertEquals("Sopot", garageDto.address());
        assertEquals(2, garageDto.places());

    }
    @Test
    void shouldEditGaragePartially() {
        EditGarageCommand command = new EditGarageCommand(null, "Gdynia", null);
        when(garageRepository.findById(1)).thenReturn(Optional.of(garage1));
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage1);
        GarageDto result = garageService.editGaragePartially(1, command);
        assertEquals("Gdynia", result.address());
        assertEquals(2, result.places());

    }
    @Test
    public void shouldAddCarToGarage() {
        when(garageRepository.findById(1)).thenReturn(Optional.of(garage1));
        when(carRepository.findById(1)).thenReturn(Optional.of(car1));
        when(garageRepository.saveAndFlush(any(Garage.class))).thenReturn(garage1);
        when(carRepository.saveAndFlush(any(Car.class))).thenReturn(car1);
        GarageDto result = garageService.addCar(1, 1);
        assertEquals(result.address(), "Warszawa");
        assertEquals(result.places(), 2);
        assertEquals(result.lpgAllowed(),true);
        assertEquals(car1.getGarage(),garage1);
    }
    @Test
    public void shouldDeleteCarFromGarage() {
        garage1.addCar(car1);
        when(garageRepository.findById(1)).thenReturn(Optional.of(garage1));
        when(carRepository.findById(1)).thenReturn(Optional.of(car1));
        garageService.deleteCarFromGarage(1, 1);
        Assertions.assertFalse(garage1.getCars().contains(car1));
    }

}