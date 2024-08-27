package pl.kurs.model.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EditBookCommand {

    private String title;
    private String category;
    private Boolean available;
    private Long version;
}
