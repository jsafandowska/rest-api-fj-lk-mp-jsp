package pl.kurs.model.dto;

import pl.kurs.model.Garage;

public record GarageDto(int id, int places, String address, boolean lpgAllowed) {
    public static GarageDto toDto(Garage garage) {
        return new GarageDto(garage.getId(),
                garage.getPlaces(),
                garage.getAddress(),
                garage.isLpgAllowed());
    }

//    jest sens to robić w przypadku garażu??
}
