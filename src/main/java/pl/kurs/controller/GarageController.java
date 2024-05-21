package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.Book;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.service.GarageIdGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("api/v1/garages")
@Slf4j
@RequiredArgsConstructor

public class GarageController {
    private final List<Garage> garages;
    private final GarageIdGenerator garageIdGenerator;

//    @PostConstruct
//    public void init() {
//        garages.add(new Garage(garageIdGenerator.getId(),  10, "Gdansk", false));
//        garages.add(new Garage(garageIdGenerator.getId(), 50, "Gdynia", true));
//    }

    @GetMapping
    public ResponseEntity<List<Garage>> findAll() {
        log.info("findAll");
        return ResponseEntity.ok(garages);
    }

    @PostMapping
    public ResponseEntity<Garage> addGarage(@RequestBody CreateGarageCommand command) {
        log.info("addGarage({})", command);
        Garage garage = new Garage(garageIdGenerator.getId(),command.getPlaces(),command.getAddress(), command.isLpgAllowed());
        garages.add(garage);
        return ResponseEntity.status(HttpStatus.CREATED).body(garage);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Garage> findGarage(@PathVariable int id) {
        log.info("findGarage({})", id);
        return ResponseEntity.ok(garages.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(GarageNotFoundException::new));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteGarage(@PathVariable int id) {
        log.info("deleteGarage({})", id);
        if (!garages.removeIf(b -> b.getId() == id)) {
            throw new GarageNotFoundException();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Garage> editGarage(@PathVariable int id, @RequestBody CreateGarageCommand command) {
        log.info("editCar({}, {})", id, command);
       Garage garage = garages.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(GarageNotFoundException::new);
        garage.setPlaces(command.getPlaces());
        garage.setAddress(command.getAddress());
        garage.setLpgAllowed(command.isLpgAllowed());
        return ResponseEntity.status(HttpStatus.OK).body(garage);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Garage> editGaragePartially(@PathVariable int id, @RequestBody EditGarageCommand command) {
        log.info("editGarage({}, {})", id, command);
        Garage garage = garages.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(GarageNotFoundException::new);
        Optional.ofNullable(command.getPlaces()).ifPresent(garage::setPlaces);
        Optional.ofNullable(command.getAddress()).ifPresent(garage::setAddress);
        Optional.ofNullable(command.getLpgAllowed()).ifPresent(garage::setLpgAllowed);
        return ResponseEntity.status(HttpStatus.OK).body(garage);
    }


}
