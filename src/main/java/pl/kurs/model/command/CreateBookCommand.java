package pl.kurs.model.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateBookCommand {

    private String title;
    private String category;
    private Integer authorId;

    public CreateBookCommand(String[] args) {
        this.title = args[0];
        this.category = args[1];
        this.authorId = Integer.parseInt(args[2]);
    }
}
