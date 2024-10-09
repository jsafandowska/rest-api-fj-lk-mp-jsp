package pl.kurs.inheritance.dto;

import lombok.Getter;
import lombok.Setter;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.inheritance.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
public class EmployeeDto extends PersonDto {
    private String position;
    private int salary;

    public EmployeeDto(int id, String name, int age, LocalDate dateOfBirth, Gender gender, String country, String position, int salary) {
        super(id, name, age, dateOfBirth, gender, country);
        this.position = position;
        this.salary = salary;
    }
}


