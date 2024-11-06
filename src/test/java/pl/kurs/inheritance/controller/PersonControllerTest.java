package pl.kurs.inheritance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.dictionary.repository.DictionaryValueRepository;
import pl.kurs.inheritance.dto.StudentDto;
import pl.kurs.inheritance.enums.Gender;
import pl.kurs.inheritance.model.PersonParameter;
import pl.kurs.inheritance.model.command.CreatePersonCommand;
import pl.kurs.inheritance.service.PersonService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
public class PersonControllerTest {
    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private PersonService personService;
    @Mock
    private DictionaryValueRepository dictionaryValueRepository;
    @Test
    void shouldCreateNewPerson() throws Exception {
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
        when(dictionaryValueRepository.findByDictionaryNameAndValue("COUNTRIES", "Polska"))
                .thenReturn(Optional.of(countryValue));
        CreatePersonCommand command = new CreatePersonCommand("Student", parameters);
        StudentDto studentDto = new StudentDto(1, "Joe", 22, LocalDate.of(2000, 1, 1), Gender.MALE, countryValue, 1500, "A1");
        when(personService.createPerson(Mockito.any(CreatePersonCommand.class))).thenReturn(studentDto);
        postman.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(studentDto.getName())))
                .andExpect(jsonPath("$.age", is(studentDto.getAge())))
                .andExpect(jsonPath("$.dateOfBirth", is(studentDto.getDateOfBirth().toString())))
                .andExpect(jsonPath("$.gender", is(studentDto.getGender().toString())))
                .andExpect(jsonPath("$.scholarship", is(studentDto.getScholarship())))
                .andExpect(jsonPath("$.group", is(studentDto.getGroup())));
//                .andExpect(jsonPath("$.country.value", is(countryValue.getValue())));
//        nie działa, potrzebny by był odzielny DTO
    }

    @Test
    public void shouldNotCreateAnyEntity_UnknownType() throws Exception {
        CreatePersonCommand command = new CreatePersonCommand();
        command.setClassType("Trololo");

        List<PersonParameter> parameters = new ArrayList<>();
        parameters.add(new PersonParameter("name", "sample name"));
        parameters.add(new PersonParameter("surname", "sample surname"));
        command.setParameters(parameters);

        String json = objectMapper.writeValueAsString(command);

        postman.perform(post("/api/v1/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("UNKNOWN_ENTITY_TYPE"));
    }
    @Test
    public void shouldNotCreatePersonWithAgeHigherThen120() throws Exception {
        List<PersonParameter> parameters = List.of(
                new PersonParameter("name", "Joe"),
                new PersonParameter("age", "150"),
                new PersonParameter("dateOfBirth", "2000-01-01"),
                new PersonParameter("gender", "MALE"),
                new PersonParameter("country", "Polska"),
                new PersonParameter("scholarship", "1500"),
                new PersonParameter("group", "A1")
                                                  );
        CreatePersonCommand command = new CreatePersonCommand();
        command.setParameters(parameters);
        command.setClassType("Student");
        String json = objectMapper.writeValueAsString(command);
        postman.perform(post("/api/v1/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.messages[0]").value("INVALID_AGE"));
    }
}


