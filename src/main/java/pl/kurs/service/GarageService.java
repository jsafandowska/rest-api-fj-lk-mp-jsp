package pl.kurs.service;
import lombok.RequiredArgsConstructor;
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
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GarageService {

    private final GarageRepository garageRepository;
    private final CarRepository carRepository;

//    @PostConstruct - dawać to tu czy w kontrolerze dopiero?
    public void init() {
        garageRepository.saveAndFlush(new Garage(2, "Warszawa", true));
        garageRepository.saveAndFlush(new Garage(3, "Piątkowska", false));
        carRepository.saveAndFlush(new Car("Mercedes", "S-class", "petrol"));
        carRepository.saveAndFlush(new Car("Audi", "RS", "petrol"));
    }

    public List<GarageDto> findAllGarages() {
        return garageRepository.findAll().stream().map(GarageDto::toDto).toList();
    }

    public GarageDto addGarage(CreateGarageCommand command) {
        Garage garage = garageRepository.saveAndFlush(new Garage(command.getPlaces(), command.getAddress(), command.isLpgAllowed()));
        return GarageDto.toDto(garage);
    }

    public GarageDto findGarage(int id) {
        return GarageDto.toDto(garageRepository.findById(id).orElseThrow(GarageNotFoundException::new));
    }

    public void deleteGarage(int id) {
        garageRepository.deleteById(id);
    }

    public GarageDto editGarage(int id, CreateGarageCommand command) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        garage.setAddress(command.getAddress());
        garage.setPlaces(command.getPlaces());
        garage.setLpgAllowed(command.isLpgAllowed());
        return GarageDto.toDto(garageRepository.saveAndFlush(garage));
    }

    public GarageDto editGaragePartially(int id, EditGarageCommand command) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        Optional.ofNullable(command.getAddress()).ifPresent(garage::setAddress);
        Optional.ofNullable(command.getPlaces()).ifPresent(garage::setPlaces);
        Optional.ofNullable(command.getLpgAllowed()).ifPresent(garage::setLpgAllowed);
        return GarageDto.toDto(garageRepository.saveAndFlush(garage));
    }

    public GarageDto addCarToGarage(int id, int carId) {
        Garage garage = garageRepository.findById(id).orElseThrow(() -> new GarageNotFoundException());
        Car car = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException());

        if (garage.getCars().contains(car)) {
            throw new IllegalStateException("Car is already in this garage");
        }
        garage.addCar(car);
        garageRepository.saveAndFlush(garage);
        carRepository.saveAndFlush(car);
        return GarageDto.toDto(garage);
    }

    public GarageDto deleteCarFromGarage(int id, int carId) {
        Garage garage = garageRepository.findById(id).orElseThrow(GarageNotFoundException::new);
        Car car = carRepository.findById(carId).orElseThrow(CarNotFoundException::new);
        garage.addCar(car);
//        właśnie tu jest dziwnie, jak nie miałam tej metody to wywalało mi wyjątek Car Not In Garage w teście kontrolera
//        jeśli zakomentowałam metodę validate - to wtedy działało nawet bez dodania garade.addCar. Jakby wywala błąd w deleteCar,
//        bo faktycznie gdy usuwamy to znajduje błąd, że nie ma tego auta w danym garazu, więc automatycznie nie działał dobrze serwis, kontroler i potem tetsy
        garage.deleteCar(car);
        garageRepository.saveAndFlush(garage);
//        a kiedy robić to save and flush, czy konieczne jest dla CarRepo?
        return GarageDto.toDto(garage);
    }
}
