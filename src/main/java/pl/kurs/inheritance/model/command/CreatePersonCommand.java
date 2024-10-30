package pl.kurs.inheritance.model.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kurs.inheritance.model.PersonParameter;
import pl.kurs.validation.annotation.CheckEntityType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePersonCommand {

    @CheckEntityType
    private String classType;
    private List<PersonParameter> parameters;

}
