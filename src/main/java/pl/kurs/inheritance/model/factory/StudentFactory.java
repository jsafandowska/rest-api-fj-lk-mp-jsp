package pl.kurs.inheritance.model.factory;

import pl.kurs.inheritance.model.Person;
import pl.kurs.inheritance.model.PersonParameter;
import pl.kurs.inheritance.model.Student;
import pl.kurs.inheritance.model.command.CreatePersonCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentFactory implements PersonFactory {

    @Override
    public Person create(CreatePersonCommand command) {
        Map<String, String> params = convertParamsToMap(command.getParameters());
        return new Student(
                params.get("name"),
                Integer.parseInt(params.get("age")),
                Integer.parseInt(params.get("scholarship")),
                params.get("group")
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