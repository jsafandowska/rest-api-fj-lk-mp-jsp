package pl.kurs.model.command;

import lombok.*;
import pl.kurs.validation.annotation.CheckDeathYear;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@CheckDeathYear
public class CreateAuthorCommand {
    private String name;
    private String surname;
    private Integer birthYear;
    private Integer deathYear;

}
