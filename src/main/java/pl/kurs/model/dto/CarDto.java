package pl.kurs.model.dto;
import pl.kurs.model.Car;

public record CarDto(int id, String brand, String model, String fuelType) {
    public static CarDto toDto(Car car) {
        return new CarDto(car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getFuelType());
    }
//public record CarDto(int id, String brand, String model, String fuelType, Integer garageId) {
//    public static CarDto toDto(Car car) {
//        Integer garageId = (car.getGarage() != null) ? car.getGarage().getId() : null;
//        return new CarDto(car.getId(),
//                car.getBrand(),
//                car.getModel(),
//                car.getFuelType(),
//                garageId);
//    }
//    wcześniej mieliśmy wyjątek, bo odwoływaliśmy się do garazu, a dopiero potem do jego id
}

