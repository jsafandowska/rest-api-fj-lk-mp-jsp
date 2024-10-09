package pl.kurs.inheritance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Student extends Person {

    private int scholarship;
    @Column(name = "std_group")
    private String group;

    public Student(int scholarship, String group) {
        this.scholarship = scholarship;
        this.group = group;
    }

    public Student(String name, int age, LocalDate dateOfBirth, Gender gender, DictionaryValue country, int scholarship, String group) {
        super(name, age, dateOfBirth, gender, country);
        this.scholarship = scholarship;
        this.group = group;
    }
}
