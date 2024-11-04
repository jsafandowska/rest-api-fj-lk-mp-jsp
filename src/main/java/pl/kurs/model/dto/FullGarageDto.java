package pl.kurs.model.dto;



import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import pl.kurs.controller.GarageController;
import pl.kurs.model.Garage;

import java.util.List;


public record FullGarageDto(int id, int places, String address, boolean lpgAllowed,  String carsLink) {
    public static FullGarageDto toDto(Garage garage) {
        String carsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GarageController.class)
                .getCarsInGarage(garage.getId())).toUri().toString();

        return new FullGarageDto(garage.getId(), garage.getPlaces(), garage.getAddress(), garage.isLpgAllowed(), carsLink);
    }
}