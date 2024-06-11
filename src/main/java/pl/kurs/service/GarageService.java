package pl.kurs.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GarageService {
    private final GarageRepository garageRepository;
    private final CarRepository carRepository;

    @PostConstruct
    public void init() {
        garageRepository.saveAndFlush(new Garage(2, "Warszawa", true));
        garageRepository.saveAndFlush(new Garage(3, "Piątkowska", false));
        carRepository.saveAndFlush(new Car("Mercedes", "S-class", "petrol"));
        carRepository.saveAndFlush(new Car("Audi", "RS", "petrol"));
    }

    public Page<GarageDto> findAll(Pageable pageable) {
        return garageRepository.findAll(pageable).map(GarageDto::toDto);
    }
    public GarageDto addGarage(CreateGarageCommand command) {
        Garage garage = new Garage(command.getPlaces(), command.getAddress(), command.isLpgAllowed());
        garage = garageRepository.saveAndFlush(garage);
        return GarageDto.toDto(garage);
    }
    public GarageDto findGarage(int id) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        return GarageDto.toDto(garage);
    }
    public void deleteGarage(int id) {
        garageRepository.deleteById(id);
    }
    public GarageDto editGarage(int id, CreateGarageCommand command) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        garage.setAddress(command.getAddress());
        garage.setPlaces(command.getPlaces());
        garage.setLpgAllowed(command.isLpgAllowed());
        garage = garageRepository.saveAndFlush(garage);
        return GarageDto.toDto(garage);
    }
    public GarageDto editGaragePartially(int id, EditGarageCommand command) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        Optional.ofNullable(command.getAddress()).ifPresent(garage::setAddress);
        Optional.ofNullable(command.getPlaces()).ifPresent(garage::setPlaces);
        Optional.ofNullable(command.getLpgAllowed()).ifPresent(garage::setLpgAllowed);
        garage = garageRepository.saveAndFlush(garage);
        return GarageDto.toDto(garage);
    }
    public GarageDto addCar(int garageId, int carId) {
        Garage garage = garageRepository.findById(garageId).orElseThrow(GarageNotFoundException::new);
        Car car = carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
        garage.addCar(car);
        carRepository.saveAndFlush(car);
        garageRepository.saveAndFlush(garage);
        return GarageDto.toDto(garage);
    }
    public GarageDto deleteCarFromGarage(int garageId, int carId) {
        Garage garage = garageRepository.findById(garageId).orElseThrow(GarageNotFoundException::new);
        Car car = carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
        garage.deleteCar(car);
        carRepository.saveAndFlush(car);
        return GarageDto.toDto(garage);
    }

}
