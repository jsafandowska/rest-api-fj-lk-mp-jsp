package pl.kurs.inheritance.model;

import jakarta.persistence.Column;
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
public class Student extends Person {

    private int scholarship;
    @Column(name = "std_group")
    private String group;

    public Student(String name, int age, int scholarship, String group) {
        super(name, age);
        this.scholarship = scholarship;
        this.group = group;
    }

    @Override
    public List<PersonParameter> getParameters() {
        return Arrays.asList(
                new PersonParameter("scholarship", String.valueOf(scholarship)),
                new PersonParameter("group", group)
        );
    }
}
