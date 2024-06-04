package pl.kurs.model;

import jakarta.persistence.*;
import lombok.*;
import pl.kurs.exceptions.TheGarageDoesNotAllowParkingLPGCars;
import pl.kurs.exceptions.TheGarageIsFull;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "garage")
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

    public Car(String brand, String model, String fuelType) {
        this.brand = brand;
        this.model = model;
        this.fuelType = fuelType;
    }
}
