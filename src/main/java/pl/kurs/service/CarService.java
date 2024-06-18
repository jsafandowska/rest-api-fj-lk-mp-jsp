package pl.kurs.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.repository.CarRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    @Transactional
    public void init() {
        carRepository.saveAndFlush(new Car("Mercedes", "S-class", "petrol"));
        carRepository.saveAndFlush(new Car("Audi", "RS", "petrol"));
    }
    public Page<Car> findAllCars(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    public Car addCar(CreateCarCommand command) {
        return carRepository.saveAndFlush(new Car(command.getBrand(), command.getModel(), command.getFuelType()));
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
}
