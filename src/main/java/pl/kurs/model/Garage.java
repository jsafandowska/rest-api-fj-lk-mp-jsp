package pl.kurs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Garage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int places;
    private String address;
    private boolean lpgAllowed;

    public Garage(int places, String address, boolean lpgAllowed) {
        this.places = places;
        this.address = address;
        this.lpgAllowed = lpgAllowed;
    }
}
