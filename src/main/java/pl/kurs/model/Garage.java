package pl.kurs.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Garage {
    private int id;
    private int places;
    private boolean lpgAllowed;
}
