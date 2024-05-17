package pl.kurs.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Car {
    private int id;
    private String brand;
    private String model;
    private String fuelType;
}
