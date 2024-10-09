package pl.kurs.inheritance.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Employee extends Person {
    @ManyToOne
    @JoinColumn(name = "position_id")
    private DictionaryValue position;
    private int salary;


    public Employee(String name, int age, LocalDate dateOfBirth, Gender gender, DictionaryValue country, DictionaryValue position, int salary) {
        super(name, age, dateOfBirth, gender, country);
        this.position = position;
        this.salary = salary;
    }
}
