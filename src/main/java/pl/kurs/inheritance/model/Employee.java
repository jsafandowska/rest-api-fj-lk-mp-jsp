package pl.kurs.inheritance.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

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

    @Override
    public List<PersonParameter> getParameters() {
        return Arrays.asList(
                new PersonParameter("position", position),
                new PersonParameter("salary", String.valueOf(salary))
        );
    }
}
