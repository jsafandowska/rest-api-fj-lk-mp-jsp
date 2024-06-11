package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.model.dto.GarageDto;
import pl.kurs.service.GarageService;

import java.util.List;

@RestController
@RequestMapping("api/v1/garages")
@Slf4j
@RequiredArgsConstructor
public class GarageController {
    private final GarageService garageService;

    @PostConstruct
    public void init() {
        garageService.init();
    }

    @GetMapping
    public ResponseEntity<List<GarageDto>> findAll() {
        log.info("findAll");
        return ResponseEntity.ok(garageService.findAll());
    }

    @PostMapping
    public ResponseEntity<GarageDto> addGarage(@RequestBody CreateGarageCommand command) {
        log.info("addGarage({})", command);
        return ResponseEntity.status(HttpStatus.CREATED).body(garageService.addGarage(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarageDto> findGarage(@PathVariable int id) {
        log.info("findGarage({})", id);
        return ResponseEntity.ok(garageService.findGarage(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GarageDto> deleteGarage(@PathVariable int id) {
        log.info("deleteGarage({})", id);
        garageService.deleteGarage(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarageDto> editGarage(@PathVariable int id, @RequestBody CreateGarageCommand command) {
        log.info("editGarage({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(garageService.editGarage(id, command));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<GarageDto> editGaragePartially(@PathVariable int id, @RequestBody EditGarageCommand command) {
        log.info("editGarage({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(garageService.editGaragePartially(id, command));
    }

    @PatchMapping("/{id}/cars/{carId}")
    public ResponseEntity<GarageDto> addCar(@PathVariable int id, @PathVariable int carId) {
        log.info("addCar({}, {})", id, carId);
        garageService.addCar(id, carId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/cars/{carId}")
    public ResponseEntity<GarageDto> deleteCarFromGarage(@PathVariable int id, @PathVariable int carId) {
        log.info("deleteCarFromGarage({}, {})", id, carId);
        garageService.deleteCarFromGarage(id, carId);
        return ResponseEntity.ok().build();
    }
}
