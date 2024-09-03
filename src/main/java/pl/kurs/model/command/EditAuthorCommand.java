package pl.kurs.model.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EditAuthorCommand {
    private String name;
    private String surname;
    private Integer deathYear;
}