package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.command.EditCarCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final GarageRepository garageRepository;

    public List<CarDto> findAll() {
        return carRepository.findAll().stream().map(CarDto::toDto).toList();
    }

    public CarDto addCar(CreateCarCommand command) {
        Garage garage = garageRepository.findById(command.getGarageId()).orElseThrow(GarageNotFoundException::new);
        Car car = new Car(command.getBrand(), command.getModel(), command.getFuelType());
        car.setGarage(garage);
        car = carRepository.saveAndFlush(car);
        return CarDto.toDto(car);
    }

    public CarDto findCar(int id) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        return CarDto.toDto(car);
    }

    public void deleteCar(int id) {
        carRepository.deleteById(id);
    }

    public CarDto editCar(int id, EditCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        car.setBrand(command.getBrand());
        car.setModel(command.getModel());
        car.setFuelType(command.getFuelType());
        car = carRepository.saveAndFlush(car);
        return CarDto.toDto(car);
    }

    public CarDto editCarPartially(int id, EditCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        Optional.ofNullable(command.getBrand()).ifPresent(car::setBrand);
        Optional.ofNullable(command.getModel()).ifPresent(car::setModel);
        Optional.ofNullable(command.getFuelType()).ifPresent(car::setFuelType);
        car = carRepository.saveAndFlush(car);
        return CarDto.toDto(car);
    }
}


