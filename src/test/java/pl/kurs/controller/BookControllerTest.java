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
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.service.AuthorService;
import pl.kurs.service.BookService;
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
    private BookService bookService;
    @Autowired
    private AuthorService authorService;


    @Test
    public void shouldReturnSingleBook() throws Exception {
        Author author = authorService.save(new CreateAuthorCommand("Adam", "Adamski", 1900, 2000));
        Book book = bookService.save(new CreateBookCommand("Old Title", "Old Category", author.getId()));
        int id = book.getId();

        postman.perform(get("/api/v1/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Old Title"))
                .andExpect(jsonPath("$.category").value("Old Category"))
                .andExpect(jsonPath("$.authorId").value(author.getId()))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.version").value(book.getVersion()));

    }


    @Test
    public void shouldAddBook() throws Exception {
        Author author = authorService.save(new CreateAuthorCommand("Adam", "Adamski", 1900, 2000));
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
                .andExpect(jsonPath("$.version").value(0))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Book saved = objectMapper.readValue(responseString, Book.class);
        Book updatedBook = bookService.findById(saved.getId()).orElseThrow();

        Assertions.assertEquals("podstawy java", updatedBook.getTitle());
        Assertions.assertEquals("NAUKOWE", updatedBook.getCategory());
        Assertions.assertEquals(saved.getId(), updatedBook.getId());
        Assertions.assertEquals(author.getId(), updatedBook.getAuthor().getId());
        Assertions.assertTrue(updatedBook.isAvailable());
        Assertions.assertEquals(0, updatedBook.getVersion());
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        Author author = authorService.save(new CreateAuthorCommand("Adam", "Adamski", 1900, 2000));
        Book book = bookService.save(new CreateBookCommand("Old Title", "Old Category", author.getId()));
        postman.perform(delete("/api/v1/books/" + book.getId()))
                .andExpect(status().isNoContent());

        boolean bookExists = bookService.findById(book.getId()).isPresent();
        Assertions.assertFalse(bookExists, "The book should be deleted from the list");
        }

    @Test
    public void shouldEditBook() throws Exception {
        Author author = authorService.save(new CreateAuthorCommand("Adam", "Adamski", 1900, 2000));
        Book book = bookService.save(new CreateBookCommand("Old Title", "Old Category", author.getId()));
        int bookId = book.getId();
        EditBookCommand command = new EditBookCommand("New Title", "New Category", false, -1L);
        String json = objectMapper.writeValueAsString(command);

        System.out.println("---------------------------------------------");
        postman.perform(put("/api/v1/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.category").value("New Category"))
                .andExpect(jsonPath("$.available").value(false))
                .andExpect(jsonPath("$.authorId").value(author.getId()))
                .andExpect(jsonPath("$.version").value(1));

        System.out.println("---------------------");
        Book updatedBook = bookService.findById(bookId).orElseThrow();
        Assertions.assertEquals("New Title", updatedBook.getTitle());
        Assertions.assertEquals("New Category", updatedBook.getCategory());
        Assertions.assertEquals(updatedBook.getId(), book.getId());
        Assertions.assertEquals(author.getId(), updatedBook.getAuthor().getId());
        Assertions.assertFalse(updatedBook.isAvailable());
        Assertions.assertEquals(1, updatedBook.getVersion());
    }

    @Test
    public void shouldEditBookPartially() throws Exception {
        Author author = authorService.save(new CreateAuthorCommand("Adam", "Adamski", 1900, 2000));
        Book book = bookService.save(new CreateBookCommand("Old Title", "Old Category", author.getId()));
        long initialVersion = book.getVersion();

        EditBookCommand command = new EditBookCommand(null, "New Category", null,null);
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
                .andExpect(jsonPath("$.version").value(initialVersion + 1))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Book saved = objectMapper.readValue(responseString, Book.class);
        Book updatedBook = bookService.findById(saved.getId()).orElseThrow();
        Assertions.assertNotNull(saved, "The book should exist in the list");
        Assertions.assertEquals(book.getId(), updatedBook.getId());
        Assertions.assertEquals("Old Title", saved.getTitle());
        Assertions.assertEquals("New Category", saved.getCategory());
        Assertions.assertTrue(saved.isAvailable());
        Assertions.assertEquals(author.getId(), updatedBook.getAuthor().getId());
        Assertions.assertEquals(initialVersion + 1, updatedBook.getVersion());
    }
}