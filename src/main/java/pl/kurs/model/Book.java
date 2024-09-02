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
    @ManyToOne
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private Author author;

    @Version
    private long version;

    public Book(String title, String category, boolean available, Author author) {
        this.title = title;
        this.category = category;
        this.available = available;
        this.author = author;
       author.getBooks().add(this);
    }
}
