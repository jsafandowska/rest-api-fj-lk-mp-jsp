package pl.kurs.inheritance.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kurs.inheritance.dto.PersonDto;
import pl.kurs.inheritance.facade.PersonFacade;
import pl.kurs.inheritance.facade.PersonFacadeFactory;
import pl.kurs.inheritance.model.command.CreatePersonCommand;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonService {

    private final PersonFacadeFactory personFacadeFactory;

    public PersonDto createPerson(CreatePersonCommand command) {
        PersonFacade facade = personFacadeFactory.getFacade(command.getType().toString());
        if (facade == null) {
            throw new IllegalArgumentException("Unknown type: " + command.getType());
        }
        try {
            return facade.addPerson(command.getParameters());
        } catch (Exception e) {
            log.error("Error creating person: {}", e.getMessage());
            throw e;
        }
    }
}


