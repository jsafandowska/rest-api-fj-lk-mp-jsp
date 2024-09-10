package pl.kurs.pesimistic.lock.example.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;


    public Visit(LocalDateTime date, Doctor doctor) {
        this.date = date;
        this.doctor = doctor;
    }
}
