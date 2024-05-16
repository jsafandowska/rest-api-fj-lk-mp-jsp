package pl.kurs.model;


import lombok.*;

import java.util.concurrent.atomic.AtomicInteger;

// Garage(id, int places, address,  boolean lpgAllowed)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Garage {
    private int id;
    private int places;
    private String address;
    private boolean lpgAllowed;
}
