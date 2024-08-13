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
import pl.kurs.inheritance.dto.StudentDto;
import pl.kurs.inheritance.model.PersonParameter;
import pl.kurs.inheritance.model.command.CreatePersonCommand;
import pl.kurs.inheritance.service.PersonService;
import java.util.List;
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


    @Test
    void shouldCreateNewPerson() throws Exception {
        List<PersonParameter> parameters = List.of(
                new PersonParameter("name", "Joe"),
                new PersonParameter("age", "22"),
                new PersonParameter("pesel", "1234557"),
                new PersonParameter("scholarship", "1500"),
                new PersonParameter("group", "A1")
        );
        CreatePersonCommand command = new CreatePersonCommand("Student", parameters);
        StudentDto studentDto = new StudentDto(1, "Joe", 22,1500,"A1");
        when(personService.createPerson(Mockito.any(CreatePersonCommand.class))).thenReturn(studentDto);
        postman.perform(post("/api/v1/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(studentDto.getName())))
                .andExpect(jsonPath("$.age", is(studentDto.getAge())))
                .andExpect(jsonPath("$.scholarship", is(studentDto.getScholarship())))
                .andExpect(jsonPath("$.group", is(studentDto.getGroup())));
    }
}

