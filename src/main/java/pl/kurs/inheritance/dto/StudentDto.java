package pl.kurs.inheritance.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class StudentDto extends PersonDto {
    private int scholarship;
    private String group;

    public StudentDto(int id, String name, int age, int scholarship, String group) {
        super(id, name, age);
        this.scholarship = scholarship;
        this.group = group;
    }
}