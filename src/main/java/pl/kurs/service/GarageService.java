package pl.kurs.service;

import jakarta.transaction.Transactional;
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
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;

import java.util.Optional;

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

    //todo zamienić garage na garageDTO

    public Page<Garage> findAllGarages(Pageable pageable) {
        return garageRepository.findAll(pageable);
    }

    public Garage addGarage(CreateGarageCommand command) {
        return garageRepository.saveAndFlush(new Garage(command.getPlaces(), command.getAddress(), command.isLpgAllowed()));
    }

    public Garage findGarage(int id) {
        return garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
    }

    public void deleteGarage(int id) {
        garageRepository.deleteById(id);
    }

    public Garage editGarage(int id, CreateGarageCommand command) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        garage.setAddress(command.getAddress());
        garage.setPlaces(command.getPlaces());
        garage.setLpgAllowed(command.isLpgAllowed());
        return garageRepository.saveAndFlush(garage);
    }

    public Garage editGaragePartially(int id, EditGarageCommand command) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        Optional.ofNullable(command.getAddress()).ifPresent(garage::setAddress);
        Optional.ofNullable(command.getPlaces()).ifPresent(garage::setPlaces);
        Optional.ofNullable(command.getLpgAllowed()).ifPresent(garage::setLpgAllowed);
        return garageRepository.saveAndFlush(garage);
    }

    public void addCarToGarage(int id, int carId) {
        Garage garage = garageRepository.findById(id).orElseThrow(() -> new GarageNotFoundException());
        Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException());
        garage.addCar(car);
        carRepository.saveAndFlush(car);
    }

    public void deleteCarFromGarage(int id, int carId) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        Car car = carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
        garage.deleteCar(car);
        carRepository.saveAndFlush(car);
    }

}
