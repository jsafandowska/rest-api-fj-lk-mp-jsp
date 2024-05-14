package pl.kurs.model.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EditGarageCommand {
    private Integer places;
    private String address;
    private Boolean lpgAllowed;
}
