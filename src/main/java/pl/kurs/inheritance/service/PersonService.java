package pl.kurs.inheritance.service;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.inheritance.dto.PersonDto;
import pl.kurs.inheritance.facade.PersonFacade;
import pl.kurs.inheritance.model.Employee;
import pl.kurs.inheritance.model.Person;
import pl.kurs.inheritance.model.Student;
import pl.kurs.inheritance.model.command.CreatePersonCommand;
import pl.kurs.inheritance.repository.PersonRepository;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;
    private final Map<String, PersonFacade> facades;

    @PostConstruct
    public void init(){
        personRepository.saveAllAndFlush(
                List.of(
                        new Employee("X", 25,"92345678123141", "programmer", 15000),
                        new Employee("Y", 45,"82345678123141", "programmer", 25000),
                        new Employee("Z", 35,"72345678123141", "programmer", 17000),
                        new Student("A", 20,"6345678123141", 1000, "1a"),
                        new Student("B", 19,"52345678123141", 0, "2a"),
                        new Student("C", 21,"42345678123141", 500, "1b")
                )
        );
    }

    public PersonDto createPerson(CreatePersonCommand command) {
        PersonFacade facade = facades.get(command.getClassType() + "Facade");
        Person person = personRepository.saveAndFlush(facade.createPerson(command.getParameters()));
        return facade.toDto(person);
    }

    @Transactional(readOnly = true)
    public List<PersonDto> findAll() {
        return personRepository.findAll().stream().map(this::mapToDto).toList();
    }

    private PersonDto mapToDto(Person person){
        PersonFacade facade = facades.get(person.getClass().getSimpleName() + "Facade");
        return facade.toDto(person);
    }
}


