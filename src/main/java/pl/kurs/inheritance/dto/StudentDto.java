package pl.kurs.inheritance.dto;

import lombok.Getter;
import lombok.Setter;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.inheritance.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
public class StudentDto extends PersonDto {
    private int scholarship;
    private String group;

    public StudentDto(int id, String name, int age, LocalDate dateOfBirth, Gender gender, DictionaryValue country, int scholarship, String group) {
        super(id, name, age, dateOfBirth, gender, country);
        this.scholarship = scholarship;
        this.group = group;
    }
}