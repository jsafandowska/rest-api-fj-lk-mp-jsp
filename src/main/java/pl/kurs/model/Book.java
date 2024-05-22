package pl.kurs.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 500)
    private String title;
    private String category;
    private boolean available;

    public Book(String title, String category, boolean available) {
        this.title = title;
        this.category = category;
        this.available = available;
    }
}
