package pl.kurs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.Main;
import pl.kurs.exceptions.BookNotFoundException;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.service.BookIdGenerator;


import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private List<Book> books;

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private BookIdGenerator bookIdGenerator;


    @Test
    public void shouldReturnSingleBook() throws Exception {
        Book book = new Book(bookIdGenerator.getId(), "Pan Tadeusz", "LEKTURA", true);
        books.add(book);

        postman.perform(get("/api/v1/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value("Pan Tadeusz"))
                .andExpect(jsonPath("$.category").value("LEKTURA"))
                .andExpect(jsonPath("$.available").value(true));
    }


    @Test
    public void shouldAddBook() throws Exception {
        CreateBookCommand command = new CreateBookCommand("podstawy java", "NAUKOWE");
        String json = objectMapper.writeValueAsString(command);

        postman.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("podstawy java"))
                .andExpect(jsonPath("$.category").value("NAUKOWE"))
                .andExpect(jsonPath("$.available").value(true));

        Book recentlyAdded = books.get(books.size() - 1);
        Assertions.assertEquals("podstawy java", recentlyAdded.getTitle());
        Assertions.assertEquals("NAUKOWE", recentlyAdded.getCategory());
        Assertions.assertTrue(recentlyAdded.isAvailable());
        Assertions.assertTrue(recentlyAdded.getId() > 0);

        Mockito.verify(bookIdGenerator, Mockito.times(1)).getId();
    }

    @Test
    public void shouldFindBook() throws Exception {
        Book book = new Book(bookIdGenerator.getId(), "podstawy java", "lektura", true);
        books.add(book);

        postman.perform(get("/api/v1/books/" + book.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value("podstawy java"))
                .andExpect(jsonPath("$.category").value("lektura"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        Book book = new Book(bookIdGenerator.getId(), "podstawy java", "lektura", true);
        books.add(book);
        postman.perform(delete("/api/v1/books/" + book.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertFalse(books.stream().anyMatch(b -> b.getId() == book.getId()));
    }

    @Test
    public void shouldEditBook() throws Exception {
        Book book = new Book(bookIdGenerator.getId(), "podstawy java", "lektura", true);
        books.add(book);
        EditBookCommand command = new EditBookCommand("abc java", "NAUKOWE", false);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(put("/api/v1/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value("abc java"))
                .andExpect(jsonPath("$.category").value("NAUKOWE"))
                .andExpect(jsonPath("$.available").value(false));

        Book updatedBook = books.stream().filter(b -> b.getId() == book.getId()).findFirst().orElseThrow(BookNotFoundException::new);
        Assertions.assertEquals("abc java", updatedBook.getTitle());
        Assertions.assertEquals("NAUKOWE", updatedBook.getCategory());
        Assertions.assertFalse(updatedBook.isAvailable());
    }

    @Test
    public void shouldPartiallyEditBook() throws Exception {
        Book book = new Book(bookIdGenerator.getId(), "podstawy java", "lektura", true);
        books.add(book);
        EditBookCommand command = new EditBookCommand(null, "TECHNICAL", false);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(patch("/api/v1/books/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(book.getId()))
                .andExpect(jsonPath("$.title").value("podstawy java")) // Title remains unchanged
                .andExpect(jsonPath("$.category").value("TECHNICAL"))
                .andExpect(jsonPath("$.available").value(false));

        Book updatedBook = books.stream().filter(b -> b.getId() == book.getId()).findFirst().orElseThrow();
        Assertions.assertEquals("podstawy java", updatedBook.getTitle());
        Assertions.assertEquals("TECHNICAL", updatedBook.getCategory());
        Assertions.assertFalse(updatedBook.isAvailable());
    }

}