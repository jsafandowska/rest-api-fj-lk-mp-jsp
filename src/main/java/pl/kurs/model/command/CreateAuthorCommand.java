package pl.kurs.model.command;

import lombok.*;
import pl.kurs.validation.annotation.CheckDeathYear;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class CreateAuthorCommand {
    private String name;
    private String surname;
    private Integer birthYear;
    @CheckDeathYear
    private Integer deathYear;

}
