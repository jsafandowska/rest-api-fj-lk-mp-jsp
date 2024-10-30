package pl.kurs.model.dto;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import pl.kurs.controller.GarageController;
import pl.kurs.model.Garage;

//public record GarageDto(int id, int places, String address, boolean lpgAllowed) {
//    public static GarageDto toDto(Garage garage) {
//        return new GarageDto(garage.getId(),
//                garage.getPlaces(),
//                garage.getAddress(),
//                garage.isLpgAllowed());
//    }
//}


public record GarageDto(int id, int places, String address, boolean lpAllowed, String carsLink) {

    public static GarageDto toDto(Garage garage) {
        String carsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GarageController.class)
                                                                    .getCarsInGarage(garage.getId())).toUri().toString();

        return new GarageDto(garage.getId(), garage.getPlaces(), garage.getAddress(), garage.isLpgAllowed(), carsLink);
    }
}