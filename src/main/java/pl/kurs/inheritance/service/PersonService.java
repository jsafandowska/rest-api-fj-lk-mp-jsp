package pl.kurs.inheritance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.inheritance.dto.PersonDto;
import pl.kurs.inheritance.facade.PersonFacade;
import pl.kurs.inheritance.model.Person;
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

    public PersonDto createPerson(CreatePersonCommand command) {
        PersonFacade facade = facades.get(command.getClassType().toLowerCase() + "Facade");
        log.info("Available facades: {}", facades.keySet());
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


