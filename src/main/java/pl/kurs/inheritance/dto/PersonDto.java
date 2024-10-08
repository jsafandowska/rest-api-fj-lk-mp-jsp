package pl.kurs.inheritance.dto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.inheritance.enums.Gender;

import java.time.LocalDate;

@Getter
@Setter
public abstract class PersonDto {
    private int id;
    private String name;
    private int age;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String country;

    public PersonDto(int id, String name, int age, LocalDate dateOfBirth, Gender gender, String country) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.country = country;
    }
}