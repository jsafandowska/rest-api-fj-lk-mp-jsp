package pl.kurs.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.kurs.validation.annotation.CheckBirthYear;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private Integer birthYear;
    private Integer deathYear;

    @OneToMany(mappedBy = "author")
    @ToString.Exclude
    private Set<Book> books = new HashSet<>();
    @Version
    private long version;


    public Author(String name, String surname, Integer birthYear, Integer deathYear) {
        this.name = name;
        this.surname = surname;
        this.birthYear = birthYear;
        this.deathYear = deathYear;
    }

    public Author(Author author) {
        this.id = author.getId();
        this.name=author.getName();
        this.surname=author.getSurname();
        this.birthYear=author.getBirthYear();
        this.deathYear=author.getDeathYear();
        this.version=author.getVersion();
    }

}
