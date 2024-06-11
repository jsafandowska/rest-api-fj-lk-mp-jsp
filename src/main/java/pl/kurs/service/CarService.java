package pl.kurs.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.repository.CarRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    @Transactional
    public void init() {
        carRepository.saveAndFlush(new Car("Mercedes", "S-class", "petrol"));
        carRepository.saveAndFlush(new Car("Audi", "RS", "petrol"));
    }
    public List<CarDto> findAll() {
        return carRepository.findAll().stream().map(CarDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public CarDto addCar(CreateCarCommand command) {
        Car car = carRepository.saveAndFlush(new Car(command.getBrand(), command.getModel(), command.getFuelType()));
        return CarDto.toDto(car);
    }

    public CarDto findCar(int id) {
        return CarDto.toDto(carRepository.findById(id).orElseThrow(CarNotFoundException::new));
    }

    @Transactional
    public void deleteCar(int id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public CarDto editCar(int id, CreateCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        car.setBrand(command.getBrand());
        car.setModel(command.getModel());
        car.setFuelType(command.getFuelType());
        return CarDto.toDto(carRepository.saveAndFlush(car));
    }

    @Transactional
    public CarDto editCarPartially(int id, CreateCarCommand command) {
        Car car = carRepository.findById(id).orElseThrow(CarNotFoundException::new);
        Optional.ofNullable(command.getBrand()).ifPresent(car::setBrand);
        Optional.ofNullable(command.getModel()).ifPresent(car::setModel);
        Optional.ofNullable(command.getFuelType()).ifPresent(car::setFuelType);
        return CarDto.toDto(carRepository.saveAndFlush(car));
    }
}
