package pl.kurs.inheritance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Student extends Person {

    private int scholarship;
    @Column(name = "std_group")
    private String group;

    public Student(String name, int age, int scholarship, String group) {
        super(name, age);
        this.scholarship = scholarship;
        this.group = group;
    }
}
