package pl.kurs.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.model.Author;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;
import pl.kurs.service.AuthorService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
public class AuthorControllerTest {
    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AuthorService authorService;

    @Test
    public void shouldReturnAuthor() throws Exception {
        Author author = authorService.addAuthor(new CreateAuthorCommand("Imie", "Nazwisko", 1900, 2000));
        postman.perform(get("/api/v1/authors/" + author.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(author.getId()))
                .andExpect(jsonPath("$.name").value(author.getName()))
                .andExpect(jsonPath("$.surname").value(author.getSurname()))
                .andExpect(jsonPath("$.birthYear").value(author.getBirthYear()))
                .andExpect(jsonPath("$.deathYear").value(author.getDeathYear()));
    }

    @Test
    public void shouldThrowExceptionWhenAuthorNotFound() throws Exception {
        int nonExistentAuthorId = 999;
        postman.perform(get("/api/v1/authors/" + nonExistentAuthorId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldAddAuthor() throws Exception {
        CreateAuthorCommand command = new CreateAuthorCommand("Imie", "Nazwisko", 1900, 2000);
        String json = objectMapper.writeValueAsString(command);
        String responseString = postman.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Imie"))
                .andExpect(jsonPath("$.surname").value("Nazwisko"))
                .andExpect(jsonPath("$.birthYear").value(1900))
                .andExpect(jsonPath("$.deathYear").value(2000))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Author saved = objectMapper.readValue(responseString, Author.class);
        Author recentlyAdded = authorService.findAuthor(saved.getId());
        Assertions.assertEquals("Imie", recentlyAdded.getName());
        Assertions.assertEquals("Nazwisko", recentlyAdded.getSurname());
        Assertions.assertEquals(1900, recentlyAdded.getBirthYear());
        Assertions.assertEquals(2000, recentlyAdded.getDeathYear());
    }

    // todo do edycji wykorzystujemy EditCommand
    @Test
    public void shouldEditAuthor() throws Exception {
        Author authorToEdit = authorService.addAuthor(new CreateAuthorCommand("Name", "Surname", 1000, 1500));
        EditAuthorCommand command = new EditAuthorCommand("Imie", "Nazwisko", 2000, 0L);
        String json = objectMapper.writeValueAsString(command);
        System.out.println("---------------------------------------------");
        String responseString = postman.perform(put("/api/v1/authors/" + authorToEdit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(authorToEdit.getId()))
                .andExpect(jsonPath("$.name").value("Imie"))
                .andExpect(jsonPath("$.surname").value("Nazwisko"))
                .andExpect(jsonPath("$.birthYear").value(1000))
                .andExpect(jsonPath("$.deathYear").value(2000))
                .andExpect(jsonPath("$.version").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("---------------------------------------------");
        Author saved = objectMapper.readValue(responseString, Author.class);
        Author recentlyAdded = authorService.findAuthor(saved.getId());
        Assertions.assertEquals("Imie", recentlyAdded.getName());
        Assertions.assertEquals("Nazwisko", recentlyAdded.getSurname());
        Assertions.assertEquals(1000, recentlyAdded.getBirthYear());
        Assertions.assertEquals(2000, recentlyAdded.getDeathYear());
        Assertions.assertEquals(1, recentlyAdded.getVersion());
    }

    // todo do edycji wykorzystujemy EditCommand
    @Test
    public void shouldEditAuthorPartially() throws Exception {
        Author authorToEdit = authorService.addAuthor(new CreateAuthorCommand("Name", "Surname", 1000, 1500));
        EditAuthorCommand command = new EditAuthorCommand("Imie", null, 2000,0L);
        String json = objectMapper.writeValueAsString(command);
        String responseString = postman.perform(patch("/api/v1/authors/" + authorToEdit.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Imie"))
                .andExpect(jsonPath("$.surname").value("Surname"))
                .andExpect(jsonPath("$.birthYear").value(1000))
                .andExpect(jsonPath("$.deathYear").value(2000))
                .andExpect(jsonPath("$.version").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();
        Author saved = objectMapper.readValue(responseString, Author.class);
        System.out.println("------------------------------------------");
        Author recentlyAdded = authorService.findAuthor(saved.getId());
        Assertions.assertEquals("Imie", recentlyAdded.getName());
        Assertions.assertEquals("Surname", recentlyAdded.getSurname());
        Assertions.assertEquals(1000, recentlyAdded.getBirthYear());
        Assertions.assertEquals(2000, recentlyAdded.getDeathYear());
        Assertions.assertEquals(1, recentlyAdded.getVersion());
    }
    @Test
    public void shouldNotAddAuthor() throws Exception {
        CreateAuthorCommand command = new CreateAuthorCommand("Imie", "Nazwisko", 1500, 2030);
        String json = objectMapper.writeValueAsString(command);
        postman.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.messages[0]").value("Invalid birth year"));
    }
}
