package pl.kurs.inheritance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.inheritance.enums.Gender;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personIdGenerator")
    @SequenceGenerator(name = "personIdGenerator", sequenceName = "person_seq", initialValue = 1, allocationSize = 100)
    private int id;
    private String name;
    private int age;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private DictionaryValue country;

    public Person(String name, int age, LocalDate dateOfBirth, Gender gender, DictionaryValue country) {
        this.name = name;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.country = country;
    }
}
