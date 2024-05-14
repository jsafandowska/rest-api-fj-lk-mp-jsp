package pl.kurs.model;

import lombok.*;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
// stworz dwie klasy Car(id, brand,model, String fuelType)

public class Car {
    private int id;
    private String brand;
    private String model;
    private String fuelType;
}
