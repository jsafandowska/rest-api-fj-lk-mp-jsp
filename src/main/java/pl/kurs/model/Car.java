package pl.kurs.model;

import jakarta.persistence.*;
import lombok.*;
import pl.kurs.exceptions.LpgNotAllowedException;
import pl.kurs.exceptions.NoParkingSpacesInTheGarageException;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String brand;
    private String model;
    private String fuelType;
    @ManyToOne
    @JoinColumn(name = "garage_id")
    private Garage garage;


    public Car(String brand, String model, String fuelType, Garage garage) {
        this.brand = brand;
        this.model = model;
        this.fuelType = fuelType;
        this.garage = garage;
        addGarage(garage);
    }

    public void addGarage(Garage garage) {
        if (garage.isLpgAllowed() == false && fuelType.equals("LPG")) {
            throw new LpgNotAllowedException();
        }
        if (garage.getPlaces() > garage.getCars().size()) {
            garage.getCars().add(this);
        } else {
            throw new NoParkingSpacesInTheGarageException();
        }
    }
}
