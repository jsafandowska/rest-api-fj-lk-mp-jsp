package pl.kurs.inheritance.model.command;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.inheritance.model.PersonParameter;

import java.util.List;
@Getter
@Setter
public class CreatePersonCommand {
        private String classType;
        private List<PersonParameter> parameters;

        public CreatePersonCommand(String classType, List<PersonParameter> parameters) {
            this.classType = classType;
            this.parameters = parameters;
        }
}
