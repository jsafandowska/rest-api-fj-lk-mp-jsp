package pl.kurs.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.repository.CarRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    @PostConstruct
    public void init() {
        carRepository.saveAndFlush(new Car("Mercedes", "S-class", "petrol"));
        carRepository.saveAndFlush(new Car("Audi", "RS", "petrol"));
    }
    public Page<CarDto> findAll(Pageable pageable) {
        return carRepository.findAll(pageable).map(CarDto::toDto);
    }
    public CarDto addCar(CreateCarCommand command) {
        Car car = new Car(command.getBrand(), command.getModel(), command.getFuelType());
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
