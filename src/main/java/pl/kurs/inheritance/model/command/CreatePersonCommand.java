package pl.kurs.inheritance.model.command;

import lombok.Getter;
import lombok.Setter;
import pl.kurs.inheritance.model.PersonParameter;

import java.util.List;
@Getter
@Setter
public class CreatePersonCommand {
    private String type;
    private List<PersonParameter> parameters;
}

