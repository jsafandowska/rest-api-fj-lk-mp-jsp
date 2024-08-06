package pl.kurs.inheritance.model.command;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.inheritance.model.PersonParameter;
import pl.kurs.inheritance.model.PersonType;

import java.util.List;
@Getter
@Setter
public class CreatePersonCommand {
    // typ
    // lista parametrow
        private PersonType type;
        private List<PersonParameter> parameters;

        public CreatePersonCommand(PersonType type, List<PersonParameter> parameters) {
            this.type = type;
            this.parameters = parameters;
        }
}
