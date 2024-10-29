package pl.kurs.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.command.CreateGarageCommand;
import pl.kurs.model.command.EditGarageCommand;
import pl.kurs.model.dto.CarDto;
import pl.kurs.model.dto.GarageDto;
import pl.kurs.service.GarageService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("api/v1/garages")
@Slf4j
@RequiredArgsConstructor
public class GarageController {

    private final GarageService garageService;

    @GetMapping("/test")
    public void test() {
        garageService.playWithTransactions();
    }
        @GetMapping("/{id}")
    @Operation(summary = "Get garage by ID")
    public ResponseEntity<GarageDto> findGarage(@PathVariable int id) {
        log.info("findGarage({})", id);
        return ResponseEntity.ok(GarageDto.toDto(garageService.findGarage(id)));
    }
//    @Operation(summary = "Get garage by ID")
//    @GetMapping("/{id}")
//    public ResponseEntity<EntityModel<GarageDto>> findGarage(@PathVariable int id) {
//        log.info("findGarage({})", id);
//        GarageDto garageDto = GarageDto.toDto(garageService.findGarage(id));
//
//        EntityModel<GarageDto> resource = EntityModel.of(garageDto);
//        resource.add(linkTo(methodOn(GarageController.class).findGarage(id)).withSelfRel());
//        resource.add(linkTo(methodOn(GarageController.class).getCarsInGarage(id)).withRel("cars"));
//
//        return ResponseEntity.ok(resource);
//    }

    @Operation(summary = "Get all cars in a specific garage")
    @GetMapping("/{id}/cars")
    public ResponseEntity<CollectionModel<CarDto>> getCarsInGarage(@PathVariable int id) {
        List<CarDto> cars = garageService.getCarsInGarage(id);

        CollectionModel<CarDto> resources = CollectionModel.of(cars);
        resources.add(linkTo(methodOn(GarageController.class).getCarsInGarage(id)).withSelfRel());

        return ResponseEntity.ok(resources);
    }

    @GetMapping
    @Operation(summary = "Get all garages")
    public ResponseEntity<Page<GarageDto>> findAll(@PageableDefault Pageable pageable) {
        log.info("findAll");
        return ResponseEntity.ok(garageService.findAllGarages(pageable).map(GarageDto::toDto));
    }

    @PostMapping
    @Operation(summary = "Add garage to garages")
    public ResponseEntity<GarageDto> addGarage(@RequestBody CreateGarageCommand command) {
        log.info("addGarage({})", command);
        return ResponseEntity.status(HttpStatus.CREATED).body(GarageDto.toDto(garageService.addGarage(command)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete garage by ID")
    public ResponseEntity<GarageDto> deleteGarage(@PathVariable int id) {
        log.info("deleteGarage({})", id);
        garageService.deleteGarage(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit garage by ID")
    public ResponseEntity<GarageDto> editGarage(@PathVariable int id, @RequestBody CreateGarageCommand command) {
        log.info("editGarage({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(GarageDto.toDto(garageService.editGarage(id, command)));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Edit garage partially by ID")
    public ResponseEntity<GarageDto> editGaragePartially(@PathVariable int id, @RequestBody EditGarageCommand command) {
        log.info("editGarage({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(GarageDto.toDto(garageService.editGaragePartially(id, command)));
    }

    @PatchMapping("/{id}/cars/{carId}")
    @Operation(summary = "Add car by its ID to the garage with the given ID")
    public ResponseEntity<GarageDto> addCar(@PathVariable int id, @PathVariable int carId) {
        log.info("addCar({}, {})", id, carId);
        garageService.addCarToGarage(id, carId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/cars/{carId}")
    @Operation(summary = "Delete car by its ID from garage with the given ID")
    public ResponseEntity<GarageDto> deleteCarFromGarage(@PathVariable int id, @PathVariable int carId) {
       log.info("deleteCarFromGarage({}, {})", id, carId);
        garageService.deleteCarFromGarage(id, carId);
        return ResponseEntity.ok().build();
    }
}
