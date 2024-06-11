package pl.kurs.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int places;
    private String address;
    private boolean lpgAllowed;
    @OneToMany(mappedBy = "garage", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Car> cars = new HashSet<>();

    public Garage(int places, String address, boolean lpgAllowed) {
        this.places = places;
        this.address = address;
        this.lpgAllowed = lpgAllowed;
    }

    public void addCar(Car car) {
        validateAddCar(car);
        cars.add(car);
        car.setGarage(this);
    }

    public void deleteCar(Car car) {
        validateDeleteCar(car);
        car.setGarage(null);
        cars.remove(car);
    }

    private void validateAddCar(Car car) {
        if(car.getGarage() != null){
            throw new IllegalStateException("CAR_IS_IN_THE_GARAGE");
        }
        if (cars.size() >= places) {
            throw new IllegalStateException("GARAGE_IS_FULL");
        }
        if (!lpgAllowed && "LPG".equals(car.getFuelType())) {
            throw new IllegalStateException("GARAGE_NOT_ACCEPT_LPG");
        }
    }

            private void validateDeleteCar(Car car) {
        if (Optional.ofNullable(car.getGarage()).map(garage -> garage.getId() != id).orElse(true)) {
            throw new IllegalStateException("CAR_NOT_IN_THE_GARAGE");
        }
    }
}



