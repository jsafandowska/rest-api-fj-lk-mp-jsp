package pl.kurs.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.model.dto.GarageDto;
import pl.kurs.service.GarageService;

@RestController
@RequestMapping("api/v1/garages")
@Slf4j
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;

    @Operation(summary = "Test transactions handling in garages")
    @GetMapping("/test")
    public void test() {
        garageService.playWithTransactions();
    }

    @Operation(summary = "Find all garages with pagination")
    @GetMapping
    public ResponseEntity<Page<GarageDto>> findAll(@PageableDefault Pageable pageable) {
        log.info("findAll");
        return ResponseEntity.ok(garageService.findAllGarages(pageable).map(GarageDto::toDto));
    }

    @Operation(summary = "Add a new garage")
    @PostMapping
    public ResponseEntity<GarageDto> addGarage(@RequestBody CreateGarageCommand command) {
        log.info("addGarage({})", command);
        return ResponseEntity.status(HttpStatus.CREATED).body(GarageDto.toDto(garageService.addGarage(command)));
    }

    @Operation(summary = "Find a specific garage by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<GarageDto> findGarage(@PathVariable int id) {
        log.info("findGarage({})", id);
        return ResponseEntity.ok(GarageDto.toDto(garageService.findGarage(id)));
    }

    @Operation(summary = "Delete a specific garage by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<GarageDto> deleteGarage(@PathVariable int id) {
        log.info("deleteGarage({})", id);
        garageService.deleteGarage(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(summary = "Edit a specific garage by its ID")
    @PutMapping("/{id}")
    public ResponseEntity<GarageDto> editGarage(@PathVariable int id, @RequestBody CreateGarageCommand command) {
        log.info("editGarage({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(GarageDto.toDto(garageService.editGarage(id, command)));
    }

    @Operation(summary = "Partially edit a specific garage by its ID")
    @PatchMapping("/{id}")
    public ResponseEntity<GarageDto> editGaragePartially(@PathVariable int id, @RequestBody EditGarageCommand command) {
        log.info("editGarage({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(GarageDto.toDto(garageService.editGaragePartially(id, command)));
    }

    @Operation(summary = "Add a car to a specific garage")
    @PatchMapping("/{id}/cars/{carId}")
    public ResponseEntity<GarageDto> addCar(@PathVariable int id, @PathVariable int carId) {
        log.info("addCar({}, {})", id, carId);
        garageService.addCarToGarage(id, carId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a car from a specific garage")
    @DeleteMapping("/{id}/cars/{carId}")
    public ResponseEntity<GarageDto> deleteCarFromGarage(@PathVariable int id, @PathVariable int carId) {
        log.info("deleteCarFromGarage({}, {})", id, carId);
        garageService.deleteCarFromGarage(id, carId);
        return ResponseEntity.ok().build();
    }
}