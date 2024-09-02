package pl.kurs.controller;

import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;
import pl.kurs.model.dto.AuthorDto;
import pl.kurs.service.AuthorService;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
public class AuthorControllerTest {

    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private AuthorService authorService;
    private CreateAuthorCommand createAuthorCommand;
    private EditAuthorCommand editAuthorCommand;
    private Author author;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        author = new Author("John", "Doe", 1970, 2020);
        createAuthorCommand = new CreateAuthorCommand("Jane", "Doe", 1980, 2021);
        editAuthorCommand = new EditAuthorCommand(null, "Doe", null, null);
    }


    @Test
    public void shouldThrowExceptionWhenAuthorNotFound() throws Exception {
        int nonExistentAuthorId = 999;
        postman.perform(get("/api/v1/authors/" + nonExistentAuthorId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddAuthor() throws Exception {;
        String json = objectMapper.writeValueAsString(createAuthorCommand);
        String responseString = postman.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.birthYear").value(1980))
                .andExpect(jsonPath("$.deathYear").value(2021))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Author saved = objectMapper.readValue(responseString, Author.class);
        Author recentlyAdded = authorService.findAuthor(saved.getId());
        Assertions.assertEquals("Jane", recentlyAdded.getName());
        Assertions.assertEquals("Doe", recentlyAdded.getSurname());
        Assertions.assertEquals(1980, recentlyAdded.getBirthYear());
        Assertions.assertEquals(2021, recentlyAdded.getDeathYear());
    }
    @Test
    public void shouldEditAuthor() throws Exception {
        Author authorToEdit = authorService.addAuthor(new CreateAuthorCommand("Name", "Surname", 1000, 1500));
        CreateAuthorCommand command = new CreateAuthorCommand("X", "Y", 1900, 2000);
        String json = objectMapper.writeValueAsString(command);
        String responseString = postman.perform(post("/api/v1/authors", authorToEdit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.birthYear").value(1900))
                .andExpect(jsonPath("$.deathYear").value(2000))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Author saved = objectMapper.readValue(responseString, Author.class);
        Author recentlyAdded = authorService.findAuthor(saved.getId());
        Assertions.assertEquals("X", recentlyAdded.getName());
        Assertions.assertEquals("Y", recentlyAdded.getSurname());
        Assertions.assertEquals(1900, recentlyAdded.getBirthYear());
        Assertions.assertEquals(2000, recentlyAdded.getDeathYear());
    }
    @Test
    public void shouldEditAuthorPartially() throws Exception {
        Author authorToEdit = authorService.addAuthor(new CreateAuthorCommand("Name", "Surname", 1800, 1880));
        String json = objectMapper.writeValueAsString(editAuthorCommand);
        String responseString = postman.perform(patch("/api/v1/authors/" + authorToEdit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Name"))
                .andExpect(jsonPath("$.surname").value("Doe"))
                .andExpect(jsonPath("$.birthYear").value(1800))
                .andExpect(jsonPath("$.deathYear").value(1880))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Author saved = objectMapper.readValue(responseString, Author.class);
        Author recentlyAdded = authorService.findAuthor(saved.getId());
        Assertions.assertEquals("X", recentlyAdded.getName());
        Assertions.assertEquals("Surname", recentlyAdded.getSurname());
        Assertions.assertEquals(1800, recentlyAdded.getBirthYear());
        Assertions.assertEquals(1880, recentlyAdded.getDeathYear());
    }
}