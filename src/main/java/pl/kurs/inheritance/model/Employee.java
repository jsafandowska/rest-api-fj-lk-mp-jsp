package pl.kurs.inheritance.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Employee extends Person {

    private String position;
    private int salary;

    public Employee(String name, int age, String position, int salary) {
        super(name, age);
        this.position = position;
        this.salary = salary;
    }
}
