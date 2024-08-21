package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.repository.CarRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public List<CarDto> findAllCars() {
        return carRepository.findAll().stream().map(CarDto::toDto).toList();
    }

    public CarDto addCar(CreateCarCommand command) {
        Car car = new Car(command.getBrand(), command.getModel(), command.getFuelType());
        car = carRepository.saveAndFlush(car);
        return CarDto.toDto(car);
    }

    public CarDto findCarById(int id) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        return CarDto.toDto(car);
    }

    public void deleteCarById(int id) {
        carRepository.deleteById(id);
    }

    public CarDto editCar(int id, CreateCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        car.setBrand(command.getBrand());
        car.setModel(command.getModel());
        car.setFuelType(command.getFuelType());
        car = carRepository.saveAndFlush(car);
        return CarDto.toDto(car);
    }

    public CarDto editCarPartially(int id, CreateCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        Optional.ofNullable(command.getBrand()).ifPresent(car::setBrand);
        Optional.ofNullable(command.getModel()).ifPresent(car::setModel);
        Optional.ofNullable(command.getFuelType()).ifPresent(car::setFuelType);
        car = carRepository.saveAndFlush(car);
        return CarDto.toDto(car);
    }
}