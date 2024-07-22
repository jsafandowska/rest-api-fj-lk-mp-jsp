package pl.kurs.model.dto;
<<<<<<< HEAD

import pl.kurs.model.Garage;

=======
import pl.kurs.model.Garage;


>>>>>>> origin/master
public record GarageDto(int id, int places, String address, boolean lpgAllowed) {
    public static GarageDto toDto(Garage garage) {
        return new GarageDto(garage.getId(),
                garage.getPlaces(),
                garage.getAddress(),
                garage.isLpgAllowed());
    }
}
