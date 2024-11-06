package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GarageService {

    private final GarageRepository garageRepository;
    private final CarRepository carRepository;


    //    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public Page<Garage> findAllGarages(Pageable pageable) {
        return garageRepository.findAll(pageable);
    }
    public List<CarDto> getCarsInGarage(int garageId) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(GarageNotFoundException::new);

        return garage.getCars().stream()
                .map(CarDto::toDto)
                .collect(Collectors.toList());
    }
    public Garage addGarage(CreateGarageCommand command) {
        return garageRepository.saveAndFlush(new Garage(command.getPlaces(), command.getAddress(), command.isLpgAllowed()));
    }

    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
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

//    @Transactional
//    public void playWithTransactions() {
//        log.info("-------------------------");
//        Garage g1 = garageRepository.findById(1).get();
//        Garage g2 = garageRepository.findById(1).get();
//        log.info("G1 == G2: " + (g1 == g2));
//        log.info("-------------------------");
//    }

//    @Transactional
//    public void playWithTransactions() {
//        log.info("-------------------------");
//        Garage g1 = garageRepository.findById(1).get();
//        Garage g2 = garageRepository.findById(2).get();
//        g1.setAddress("zmieniony");
//        garageRepository.saveAndFlush(g1);
//        g2.setAddress("ZMIENIONY");
//
//        log.info("-------------------------");
//    }
//    @Transactional
//    public void playWithTransactions() {
//        log.info("-------------------------");
//        Garage g1 = garageRepository.findById(1).get();
//        log.info("{} ma samochodow: {}", g1.getAddress(), g1.getCars().size());
//        log.info("-------------------------");
//    }

//    @Transactional
//    public void playWithTransactions() {
//        log.info("-------------------------");
//        for(Garage g: garageRepository.findAllWithCars()){
//            log.info("{} ma samochodow: {}", g.getAddress(), g.getCars().size());
//        }
//        log.info("-------------------------");
//    }

    @Transactional
    @SneakyThrows
    public void playWithTransactions() {
        log.info("-------------------------");
        Garage g1 = garageRepository.findById(1).get();
        g1.setAddress("zmieniony");
        garageRepository.saveAndFlush(g1);
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("-------------------------");
        throw new Exception("Wycofanie transakcji");
    }
}