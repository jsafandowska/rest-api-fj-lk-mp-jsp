package pl.kurs.model.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateAuthorCommand {
    private String name;
    private String surname;
    private Integer birthYear;
    private Integer deathYear;

}
