package pl.kurs.inheritance.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonParameter {
    private String name;
    private String value;
    public PersonParameter(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
