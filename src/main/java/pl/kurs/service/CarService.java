package pl.kurs.service;
<<<<<<< HEAD

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
=======
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
>>>>>>> origin/master
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
<<<<<<< HEAD
import pl.kurs.repository.CarRepository;

=======
import pl.kurs.model.dto.CarDto;
import pl.kurs.repository.CarRepository;
import java.util.List;
>>>>>>> origin/master
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
<<<<<<< HEAD
    @Transactional
=======
    @PostConstruct
>>>>>>> origin/master
    public void init() {
        carRepository.saveAndFlush(new Car("Mercedes", "S-class", "petrol"));
        carRepository.saveAndFlush(new Car("Audi", "RS", "petrol"));
    }
<<<<<<< HEAD
    public Page<Car> findAllCars(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    public Car addCar(CreateCarCommand command) {
        return carRepository.saveAndFlush(new Car(command.getBrand(), command.getModel(), command.getFuelType()));
    }


    public Car findCarById(int id) {
        return carRepository.findById(id).orElseThrow(CarNotFoundException::new);
=======

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
>>>>>>> origin/master
    }

    public void deleteCarById(int id) {
        carRepository.deleteById(id);
    }

<<<<<<< HEAD
    public Car editCar(int id, CreateCarCommand command) {
=======
    public CarDto editCar(int id, CreateCarCommand command) {
>>>>>>> origin/master
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        car.setBrand(command.getBrand());
        car.setModel(command.getModel());
        car.setFuelType(command.getFuelType());
<<<<<<< HEAD
        return carRepository.saveAndFlush(car);
    }

    public Car editCarPartially(int id, CreateCarCommand command) {
=======
        car = carRepository.saveAndFlush(car);
        return CarDto.toDto(car);
    }

    public CarDto editCarPartially(int id, CreateCarCommand command) {
>>>>>>> origin/master
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        Optional.ofNullable(command.getBrand()).ifPresent(car::setBrand);
        Optional.ofNullable(command.getModel()).ifPresent(car::setModel);
        Optional.ofNullable(command.getFuelType()).ifPresent(car::setFuelType);
<<<<<<< HEAD
        return carRepository.saveAndFlush(car);
    }
}
=======
        car = carRepository.saveAndFlush(car);
        return CarDto.toDto(car);
    }
}
>>>>>>> origin/master
