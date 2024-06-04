package pl.kurs.model;

import jakarta.persistence.*;
import lombok.*;

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
    @ToString.Exclude
    private Garage garage;


    public Car(String brand, String model, String fuelType, Garage garage) {
        this.brand = brand;
        this.model = model;
        this.fuelType = fuelType;
        this.garage = garage;
        garage.getCars().add(this);
    }
}
