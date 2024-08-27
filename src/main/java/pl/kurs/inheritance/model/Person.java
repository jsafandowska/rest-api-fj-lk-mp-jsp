package pl.kurs.inheritance.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(unique = true)
    private String pesel;
    private LocalDate birthDate;
    private String gender;

    public Person(String name, int age, String pesel) {
        this.name = name;
        this.age = age;
        this.pesel = pesel;
    }
}
