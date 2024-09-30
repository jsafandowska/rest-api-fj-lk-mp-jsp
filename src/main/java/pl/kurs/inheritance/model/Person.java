package pl.kurs.inheritance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kurs.inheritance.enums.Country;
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
    // zamienic gender na enum, w bazie ma byc przechowywane jako string, @Enumerated

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Country country;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
