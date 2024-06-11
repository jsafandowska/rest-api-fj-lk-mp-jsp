package pl.kurs.model.dto;

import pl.kurs.model.Garage;

import java.util.List;


public record GarageDto(int id, int places, String address, boolean lpgAllowed, List<CarDto> cars) {

    public static GarageDto toDto(Garage garage) {
        List<CarDto> carDtos = garage.getCars().stream()
                                     .map(CarDto::toDto)
                                     .toList();
        return new GarageDto(
                garage.getId(),
                garage.getPlaces(),
                garage.getAddress(),
                garage.isLpgAllowed(),
                carDtos
        );
    }
}

