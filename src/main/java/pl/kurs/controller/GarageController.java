package pl.kurs.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateGarageCommand;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("api/v1/garages")
@Slf4j
public class GarageController {
    private List<Garage> garages = new ArrayList<>();
    private AtomicInteger generator = new AtomicInteger(0);

    @GetMapping
    public ResponseEntity<List<Garage>> findAll() {
        log.info("findAll");
        return ResponseEntity.ok(garages);
    }

    @PostMapping
    public ResponseEntity<Garage> addGarage(@RequestBody CreateGarageCommand command) {
        log.info("addGarage({})", command);
        Garage garage = new Garage(generator.incrementAndGet(), command.getPlaces(), command.getAddress(), command.isLpgAllowed());
        garages.add(garage);
        return ResponseEntity.status(HttpStatus.CREATED).body(garage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Garage> findGarage(@PathVariable int id) {
        log.info("findGarage({})", id);
        return ResponseEntity.ok(garages.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(GarageNotFoundException::new));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Garage> deleteGarage(@PathVariable int id) {
        log.info("deleteGarage({})", id);
        if (!garages.removeIf(b -> b.getId() == id)) {
            throw new GarageNotFoundException();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Garage> editGarage(@PathVariable int id, @RequestBody CreateGarageCommand command) {
        log.info("editGarage({}, {})", id, command);
        Garage garage = garages.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(GarageNotFoundException::new);
        garage.setAddress(command.getAddress());
        garage.setPlaces(command.getPlaces());
        garage.setLpgAllowed(command.isLpgAllowed());
        return ResponseEntity.status(HttpStatus.OK).body(garage);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Garage> editGaragePartially(@PathVariable int id, @RequestBody CreateGarageCommand command) {
        log.info("editGarage({}, {})", id, command);
        Garage garage = garages.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(GarageNotFoundException::new);
        Optional.ofNullable(command.getAddress()).ifPresent(garage::setAddress);
        Optional.ofNullable(command.getPlaces()).ifPresent(garage::setPlaces);
        Optional.ofNullable(command.isLpgAllowed()).ifPresent(garage::setLpgAllowed);
        return ResponseEntity.status(HttpStatus.OK).body(garage);
    }
}
