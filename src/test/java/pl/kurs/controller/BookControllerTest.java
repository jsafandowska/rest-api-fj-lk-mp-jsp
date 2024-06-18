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
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
@ActiveProfiles("tests")
class BookControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;


    @Test
    public void shouldReturnSingleBook() throws Exception {
        Author author = authorRepository.findAllWithBooks().get(0);
        Book book = bookRepository.saveAndFlush(new Book("Pan Tadeusz", "LEKTURA", true, author));
        int id = book.getId();
        postman.perform(get("/api/v1/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Pan Tadeusz"))
                .andExpect(jsonPath("$.category").value("LEKTURA"))
                .andExpect(jsonPath("$.authorId").value(author.getId()))
                .andExpect(jsonPath("$.available").value(true));
    }


    @Test
    public void shouldAddBook() throws Exception {
        Author author = authorRepository.findAll().get(0);
        CreateBookCommand command = new CreateBookCommand("podstawy java", "NAUKOWE", author.getId());
        String json = objectMapper.writeValueAsString(command);
        String responseString = postman.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("podstawy java"))
                .andExpect(jsonPath("$.category").value("NAUKOWE"))
                .andExpect(jsonPath("$.authorId").value(author.getId()))
                .andExpect(jsonPath("$.available").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Book saved = objectMapper.readValue(responseString, Book.class);
        Book recentlyAdded = bookRepository.findById(saved.getId()).get();

        Assertions.assertEquals("podstawy java", recentlyAdded.getTitle());
        Assertions.assertEquals("NAUKOWE", recentlyAdded.getCategory());
        Assertions.assertEquals(saved.getId(), recentlyAdded.getId());
        Assertions.assertEquals(author.getId(), recentlyAdded.getAuthor().getId());
        Assertions.assertTrue(recentlyAdded.isAvailable());
        Assertions.assertTrue(recentlyAdded.getId() > 0);
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        Author author = authorRepository.findAllWithBooks().get(0);
        Book bookToDelete = bookRepository.saveAndFlush(new Book("Some Title", "Some Category", true,author));
        postman.perform(delete("/api/v1/books/" + bookToDelete.getId()))
                .andExpect(status().isNoContent());
        boolean bookExists = bookRepository.existsById(bookToDelete.getId());
        Assertions.assertFalse(bookExists, "The book should be deleted from the list");
    }

    @Test
    public void shouldEditBook() throws Exception {
        Author author = authorRepository.findAllWithBooks().get(0);
        Book book = bookRepository.saveAndFlush(new Book("Old Title", "Old Category", true,author));
        EditBookCommand command = new EditBookCommand("New Title", "New Category", false);
        String json = objectMapper.writeValueAsString(command);

        String responseString = postman.perform(put("/api/v1/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.category").value("New Category"))
                .andExpect(jsonPath("$.available").value(false))
                .andExpect(jsonPath("$.authorId").value(author.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Book saved = objectMapper.readValue(responseString, Book.class);
        Book recentlyAdded = bookRepository.findById(saved.getId()).get();
        Assertions.assertNotNull(recentlyAdded, "The book should exist in the list");
        Assertions.assertEquals("New Title", recentlyAdded.getTitle());
        Assertions.assertEquals("New Category", recentlyAdded.getCategory());
        Assertions.assertEquals(recentlyAdded.getId(),book.getId());
        Assertions.assertEquals(author.getId(), recentlyAdded.getAuthor().getId());
        Assertions.assertFalse(recentlyAdded.isAvailable());
    }

    @Test
    public void shouldEditBookPartially() throws Exception {
        Author author = authorRepository.findAllWithBooks().get(0);
        Book book = bookRepository.saveAndFlush(new Book("Old Title", "Old Category", true,author));
        EditBookCommand command = new EditBookCommand(null, "New Category", null);
        String json = objectMapper.writeValueAsString(command);
        String responseString = postman.perform(patch("/api/v1/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value("Old Title"))
                .andExpect(jsonPath("$.category").value("New Category"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.authorId").value(author.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Book saved = objectMapper.readValue(responseString, Book.class);
        Book recentlyAdded = bookRepository.findById(saved.getId()).get();
        Assertions.assertNotNull(saved, "The book should exist in the list");
        Assertions.assertEquals(book.getId(), recentlyAdded.getId());
        Assertions.assertEquals("Old Title", saved.getTitle());
        Assertions.assertEquals("New Category", saved.getCategory());
        Assertions.assertTrue(saved.isAvailable());
        Assertions.assertEquals(author.getId(), recentlyAdded.getAuthor().getId());
    }
}