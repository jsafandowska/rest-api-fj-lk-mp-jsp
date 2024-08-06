package pl.kurs.inheritance.facade;

import org.springframework.stereotype.Service;
import pl.kurs.inheritance.model.factory.PersonFactory;
import pl.kurs.inheritance.model.Person;
import pl.kurs.inheritance.model.command.CreatePersonCommand;
import pl.kurs.inheritance.model.factory.EmployeeFactory;
import pl.kurs.inheritance.model.factory.StudentFactory;
import pl.kurs.inheritance.repository.PersonRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class PersonFacade {

    private final Map<String, PersonFactory> factories = new HashMap<>();
    private final PersonRepository personRepository;

    public PersonFacade(PersonRepository personRepository) {
        this.personRepository = personRepository;
        factories.put("employee", new EmployeeFactory());
        factories.put("student", new StudentFactory());
    }

    public Person createPerson(CreatePersonCommand command) {
        PersonFactory factory = factories.get(command.getType().toLowerCase());
        if (factory == null) {
            throw new IllegalArgumentException("Unknown person type: " + command.getType());
        }
        Person person = factory.create(command);
        personRepository.save(person);
        return person;
    }
}