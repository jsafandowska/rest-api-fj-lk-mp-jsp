package pl.kurs.inheritance.model.factory;

import org.springframework.stereotype.Component;
import pl.kurs.inheritance.model.Employee;
import pl.kurs.inheritance.model.Person;
import pl.kurs.inheritance.model.PersonParameter;
import pl.kurs.inheritance.model.command.CreatePersonCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("employee")
public class EmployeeFactory implements PersonFactory {

    @Override
    public Person create(CreatePersonCommand command) {
        Map<String, String> params = convertParamsToMap(command.getParameters());
        return new Employee(
                params.get("name"),
                Integer.parseInt(params.get("age")),
                params.get("position"),
                Integer.parseInt(params.get("salary"))
        );
    }

    private Map<String, String> convertParamsToMap(List<PersonParameter> parameters) {
        Map<String, String> paramMap = new HashMap<>();
        for (PersonParameter param : parameters) {
            paramMap.put(param.getName(), param.getValue());
        }
        return paramMap;
    }
}