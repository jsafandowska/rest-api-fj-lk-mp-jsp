package pl.kurs.inheritance.service;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.kurs.Main;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.inheritance.dto.PersonDto;
import pl.kurs.inheritance.dto.StudentDto;
import pl.kurs.inheritance.enums.Gender;
import pl.kurs.inheritance.facade.PersonFacade;
import pl.kurs.inheritance.model.PersonParameter;
import pl.kurs.inheritance.model.Student;
import pl.kurs.inheritance.model.command.CreatePersonCommand;
import pl.kurs.inheritance.repository.PersonRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private Map<String, PersonFacade> facades;

    @Mock
    private PersonFacade studentFacade;

    @InjectMocks
    private PersonService personService;

    @Test
    void shouldCreatePerson() {
        List<PersonParameter> parameters = List.of(
                new PersonParameter("name", "Joe"),
                new PersonParameter("age", "22"),
                new PersonParameter("dateOfBirth", "2000-01-01"),
                new PersonParameter("gender", "MALE"),
                new PersonParameter("country", "Polska"),
                new PersonParameter("scholarship", "1500"),
                new PersonParameter("group", "A1")
        );
        DictionaryValue countryValue = new DictionaryValue("Polska", new Dictionary("COUNTRIES"));
        CreatePersonCommand command = new CreatePersonCommand("Student", parameters);
        Student student = new Student("Joe", 22,  LocalDate.of(1995,1,1),Gender.MALE, countryValue,1500,"A1" );
        StudentDto expectedStudentDto = new StudentDto(1, "Joe", 22, LocalDate.of(1995,1,1), Gender.MALE,"Polska",1000,"A1");
        when(facades.get("studentFacade")).thenReturn(studentFacade);
        when(studentFacade.createPerson(parameters)).thenReturn(student);
        when(personRepository.saveAndFlush(student)).thenReturn(student);
        when(studentFacade.toDto(student)).thenReturn(expectedStudentDto);
        PersonDto actualStudentDto = personService.createPerson(command);
        assertEquals(expectedStudentDto, actualStudentDto);
    }
}
