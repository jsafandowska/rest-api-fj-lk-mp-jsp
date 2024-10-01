package pl.kurs.inheritance.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public abstract class PersonDto {
    private int id;
    private String name;
    private int age;
    private String country;

    public PersonDto(int id, String name, int age, String country) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.country = country;
    }
}