package pl.kurs.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.model.dto.GarageDto;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GarageService {

    private final GarageRepository garageRepository;
    private final CarRepository carRepository;

    @Transactional
    public void init() {
        garageRepository.saveAndFlush(new Garage(2, "Warszawa", true));
        garageRepository.saveAndFlush(new Garage(3, "Piątkowska", false));
    }

    public List<GarageDto> findAll() {
        return garageRepository.findAll().stream().map(GarageDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public GarageDto addGarage(CreateGarageCommand command) {
        Garage garage = garageRepository.saveAndFlush(new Garage(command.getPlaces(), command.getAddress(), command.isLpgAllowed()));
        return GarageDto.toDto(garage);
    }

    public GarageDto findGarage(int id) {
        return GarageDto.toDto(garageRepository.findById(id).orElseThrow(GarageNotFoundException::new));
    }

    @Transactional
    public void deleteGarage(int id) {
        garageRepository.deleteById(id);
    }

    @Transactional
    public GarageDto editGarage(int id, CreateGarageCommand command) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        garage.setAddress(command.getAddress());
        garage.setPlaces(command.getPlaces());
        garage.setLpgAllowed(command.isLpgAllowed());
        return GarageDto.toDto(garageRepository.saveAndFlush(garage));
    }

    @Transactional
    public GarageDto editGaragePartially(int id, EditGarageCommand command) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        Optional.ofNullable(command.getAddress()).ifPresent(garage::setAddress);
        Optional.ofNullable(command.getPlaces()).ifPresent(garage::setPlaces);
        Optional.ofNullable(command.getLpgAllowed()).ifPresent(garage::setLpgAllowed);
        return GarageDto.toDto(garageRepository.saveAndFlush(garage));
    }

    @Transactional
    public void addCar(int id, int carId) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        Car car = carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
        garage.addCar(car);
        carRepository.saveAndFlush(car);
    }

    @Transactional
    public void deleteCarFromGarage(int id, int carId) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        Car car = carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
        garage.deleteCar(car);
        carRepository.saveAndFlush(car);
    }
}
