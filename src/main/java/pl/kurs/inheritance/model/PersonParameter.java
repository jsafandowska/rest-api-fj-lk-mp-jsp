package pl.kurs.inheritance.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonParameter {
    //    name
//    value
    private String name;
    private Object value;
    public PersonParameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
