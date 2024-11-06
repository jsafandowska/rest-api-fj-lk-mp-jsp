package pl.kurs.model.command;

import lombok.*;
import pl.kurs.validation.annotation.CheckBirthYear;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateAuthorCommand {
    private String name;
    private String surname;
    @CheckBirthYear
    private Integer birthYear;
    private Integer deathYear;

}
