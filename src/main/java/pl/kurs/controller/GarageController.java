package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("api/v1/garages")
@Slf4j
public class GarageController {
    private List<Garage> garages = new ArrayList<>();
    private AtomicInteger generator = new AtomicInteger(0);
    @PostConstruct
    public void init(){
        garages.add(new Garage(generator.incrementAndGet(), 10, true));
        garages.add(new Garage(generator.incrementAndGet(), 20, false));
    }
    @GetMapping
    public ResponseEntity<List<Garage>> findAll(){
        log.info("findAll");
        return ResponseEntity.ok(garages);
    }

    @PostMapping
    public ResponseEntity<Garage> addGarage(@RequestBody CreateGarageCommand command){
        log.info("addCar({}))", command);
        Garage garage = new Garage(generator.incrementAndGet(), command.getPlaces(), command.isLpgAllowed());
        garages.add(garage);
        return ResponseEntity.status(HttpStatus.CREATED).body(garage);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Garage> findGarage(@PathVariable int id){
        log.info("findCar({})", id);
        return ResponseEntity.ok(garages.stream().filter(x -> x.getId() == id).findFirst().orElseThrow(GarageNotFoundException::new));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Garage> deleteGarage(@PathVariable int id){
        log.info("deleteGarage({})", id);
        if (!garages.removeIf(x -> x.getId() == id)){
            throw new GarageNotFoundException();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
