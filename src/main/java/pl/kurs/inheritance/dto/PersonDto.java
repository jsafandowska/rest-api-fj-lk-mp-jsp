package pl.kurs.inheritance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.inheritance.model.Person;
import pl.kurs.inheritance.model.PersonParameter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class PersonDto {
    private int id;
    private String name;
    private int age;
    private List<PersonParameter> parameters;

    public PersonDto(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.age = person.getAge();
        this.parameters = new ArrayList<>(person.getParameters());
    }
}