package pl.kurs.model.dto;

import lombok.Value;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;

import java.util.Set;

public record GarageDto(int id, int places, String address, boolean lpgAllowed, Set<Car> cars) {

    public static GarageDto toDto(Garage garage) {
        return new GarageDto(garage.getId(),
                garage.getPlaces(),
                garage.getAddress(),
                garage.isLpgAllowed(),
                garage.getCars());
    }
}
