package pl.kurs.model.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateCarCommand {
    private String brand;
    private String model;
    private String fuelType;
}
