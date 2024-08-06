package pl.kurs.inheritance.facade;

import pl.kurs.inheritance.model.Employee;
import pl.kurs.inheritance.model.Person;
import pl.kurs.inheritance.model.PersonParameter;
import pl.kurs.inheritance.model.Student;
import pl.kurs.inheritance.model.command.CreatePersonCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PersonFacade {
    private static final Map<String, Function<Map<String, String>, Person>> personCreators = new HashMap<>();

    static {
        personCreators.put("Employee", PersonFacade::createEmployee);
        personCreators.put("Student", PersonFacade::createStudent);
    }

    public static Person createPerson(CreatePersonCommand command) {
        Function<Map<String, String>, Person> creator = personCreators.get(command.getType());
        if (creator == null) {
            throw new IllegalArgumentException("Unknown person type: " + command.getType());
        }
        Map<String, String> paramMap = new HashMap<>();
        for (PersonParameter param : command.getParameters()) {
            paramMap.put(param.getName(), param.getValue());
        }
        return creator.apply(paramMap);
    }

    private static Employee createEmployee(Map<String, String> params) {
        String name = params.get("name");
        int age = Integer.parseInt(params.get("age"));
        String position = params.get("position");
        int salary = Integer.parseInt(params.get("salary"));
        return new Employee(name, age, position, salary);
    }

    private static Student createStudent(Map<String, String> params) {
        String name = params.get("name");
        int age = Integer.parseInt(params.get("age"));
        int scholarship = Integer.parseInt(params.get("scholarship"));
        String group = params.get("group");
        return new Student(name, age, scholarship, group);
    }
}

