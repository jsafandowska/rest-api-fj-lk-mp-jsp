package pl.kurs.model.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EditCarCommand {
    private String brand;
    private String model;
    private String fuelType;
}
