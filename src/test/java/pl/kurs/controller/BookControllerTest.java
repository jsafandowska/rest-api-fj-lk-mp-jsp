package pl.kurs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.service.BookIdGenerator;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class BookControllerTest {

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
        books.add(new Book(bookIdGenerator.getId(), "Pan Tadeusz", "LEKTURA", true));

        postman.perform(get("/api/v1/books/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
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
    public void shouldReturnAllBooks() throws Exception {
        books.add(new Book(bookIdGenerator.getId(), "Pan Tadeusz", "LEKTURA", true));
        books.add(new Book(bookIdGenerator.getId(), "Jas i Malgosia", "LEKTURA", true));
        postman.perform(get("/api/v1/books"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].title").value("Pan Tadeusz"))
               .andExpect(jsonPath("$[0].category").value("LEKTURA"))
               .andExpect(jsonPath("$[0].available").value(true))
               .andExpect(jsonPath("$[1].id").value(2))
               .andExpect(jsonPath("$[1].title").value("Jas i Malgosia"))
               .andExpect(jsonPath("$[1].category").value("LEKTURA"))
               .andExpect(jsonPath("$[1].available").value(true));
    }

    @Test
    public void shouldDeleteOneBook() throws Exception {
        books.add(new Book(bookIdGenerator.getId(), "Pan Tadeusz", "LEKTURA", true));
        books.add(new Book(bookIdGenerator.getId(), "Jas i Malgosia", "LEKTURA", true));
        postman.perform(delete("/api/v1/books/1"))
                .andExpect(status().isNoContent());
        Assertions.assertEquals(books.size(), 1);
    }

    @Test
    public void shouldEditBook() throws Exception {
        books.add(new Book(bookIdGenerator.getId(), "Stara Ksiazka", "FANTASTYKA", false));
        EditBookCommand command = new EditBookCommand("Nowa Ksiazka", "NAUKOWE", true);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(put("/api/v1/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.title").value("Nowa Ksiazka"))
               .andExpect(jsonPath("$.category").value("NAUKOWE"))
               .andExpect(jsonPath("$.available").value(true));

        Book updatedBook = books.get(0);
        Assertions.assertEquals("Nowa Ksiazka", updatedBook.getTitle());
        Assertions.assertEquals("NAUKOWE", updatedBook.getCategory());
        Assertions.assertTrue(updatedBook.isAvailable());
    }

    @Test
    public void shouldPartiallyEditBook() throws Exception {
        books.add(new Book(bookIdGenerator.getId(), "Stara Ksiazka", "FANTASTYKA", false));
        EditBookCommand command = new EditBookCommand(null, "NAUKOWE", true);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(patch("/api/v1/books/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.title").value("Stara Ksiazka"))
               .andExpect(jsonPath("$.category").value("NAUKOWE"))
               .andExpect(jsonPath("$.available").value(true));

        Book partiallyUpdatedBook = books.get(0);
        Assertions.assertEquals("Stara Ksiazka", partiallyUpdatedBook.getTitle());
        Assertions.assertEquals("NAUKOWE", partiallyUpdatedBook.getCategory());
        Assertions.assertTrue(partiallyUpdatedBook.isAvailable());
    }
}