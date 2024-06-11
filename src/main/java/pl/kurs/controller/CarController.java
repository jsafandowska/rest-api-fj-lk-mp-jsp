package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.command.CreateCarCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.service.CarService;

import java.util.List;

@RestController
@RequestMapping("api/v1/cars")
@Slf4j
@RequiredArgsConstructor
public class CarController {


    private final CarService carService;

    @PostConstruct
    public void init() {
        carService.init();
    }

    @GetMapping
    public ResponseEntity<List<CarDto>> findAll() {
        log.info("findAll");
        return ResponseEntity.ok(carService.findAll());
    }

    @PostMapping
    public ResponseEntity<CarDto> addCar(@RequestBody CreateCarCommand command) {
        log.info("addCar({})", command);
        return ResponseEntity.status(HttpStatus.CREATED).body(carService.addCar(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> findCar(@PathVariable int id) {
        log.info("findCar({})", id);
        return ResponseEntity.ok(carService.findCar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CarDto> deleteCar(@PathVariable int id) {
        log.info("deleteCar({})", id);
        carService.deleteCar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDto> editCar(@PathVariable int id, @RequestBody CreateCarCommand command) {
        log.info("editCar({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(carService.editCar(id, command));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CarDto> editCarPartially(@PathVariable int id, @RequestBody CreateCarCommand command) {
        log.info("editCar({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(carService.editCarPartially(id, command));
    }

}
