package pl.kurs.model.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateGarageCommand {
    private int places;
    private boolean lpgAllowed;
}
