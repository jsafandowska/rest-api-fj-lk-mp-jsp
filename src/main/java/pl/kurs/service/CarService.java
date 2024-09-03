package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Book;
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

    public Page<Car> findAllCars(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    public Car addCar(CreateCarCommand command) {
        Car car = new Car(command.getBrand(), command.getModel(), command.getFuelType());
        return carRepository.saveAndFlush(car);
    }

    public Car findCarById(int id) {
        return carRepository.findById(id).orElseThrow(CarNotFoundException::new);
    }

    public void deleteCarById(int id) {
        carRepository.deleteById(id);
    }

    public Car editCar(int id, CreateCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        car.setBrand(command.getBrand());
        car.setModel(command.getModel());
        car.setFuelType(command.getFuelType());
        return carRepository.saveAndFlush(car);
    }

    public Car editCarPartially(int id, CreateCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        Optional.ofNullable(command.getBrand()).ifPresent(car::setBrand);
        Optional.ofNullable(command.getModel()).ifPresent(car::setModel);
        Optional.ofNullable(command.getFuelType()).ifPresent(car::setFuelType);
        return carRepository.saveAndFlush(car);
    }
    public Optional<Car> findById(int id) {
        return carRepository.findById(id);
    }
}