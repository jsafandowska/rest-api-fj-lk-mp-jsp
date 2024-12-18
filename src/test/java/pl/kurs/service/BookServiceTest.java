package pl.kurs.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private BookService bookService;
    private Book book1;
    private Book book2;
    private Author author;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        author = new Author("Kazimierz", "Wielki", 1900, 2000);
        book1 = new Book("Ogniem i mieczem", "LEKTURA", true, author);
        book2 = new Book("Ogniem i mieczem 2", "LEKTURA", true, author);


    }
//    @Test
//    public void shouldReturnAllBooks() {
//        Pageable pageable = PageRequest.of(0, 2);
//        Page<Book> page = new PageImpl<>(Arrays.asList(book1, book2), pageable, 2);
//        when(bookRepository.findAll(pageable)).thenReturn(page);
//        Page<Book> result = bookService.findAll(pageable);
//        assertEquals(2, result.getTotalElements());
//        assertEquals("Ogniem i mieczem", result.getContent().get(0).getTitle());
//        assertEquals("Ogniem i mieczem 2", result.getContent().get(1).getTitle());
//        verify(bookRepository, times(1)).findAll(pageable);
//    }

    @Test
    public void shouldAddBook() {
        Book book = new Book("New Book", "CATEGORY", true, author);
        CreateBookCommand command = new CreateBookCommand("New Book", "CATEGORY", 1);
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book);
        Book result = bookService.addBook(command);
        assertEquals("New Book", result.getTitle());
        verify(bookRepository).saveAndFlush(any(Book.class));
    }


    @Test
    public void shouldFindBook() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book1));
        Book result = bookService.findBook(1);
        assertEquals("Ogniem i mieczem", result.getTitle());
        verify(bookRepository, times(1)).findById(1);
    }

    @Test
    public void shouldDeleteBook() {
        bookService.deleteBook(1);
        verify(bookRepository, times(1)).deleteById(1);
    }

    @Test
    public void shouldEditBook() {
        EditBookCommand command =  new EditBookCommand("New Title", "New Category", false,0L);
        when(bookRepository.findById(1)).thenReturn(Optional.of(book1));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(new Book("New Title","New Category", false, author));
        Book result = bookService.editBook(1, command);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Category", result.getCategory());
        assertFalse(result.isAvailable());
    }

    @Test
    public void shouldEditBookPartially() {
        EditBookCommand command = new EditBookCommand(null, "New Category", false,0L);
        when(bookRepository.findById(1)).thenReturn(Optional.of(book1));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book1);
        Book result = bookService.editBookPartially(1, command);
        assertEquals("Ogniem i mieczem", result.getTitle());
        assertEquals("New Category", result.getCategory());
        verify(bookRepository, times(1)).findById(1);
        verify(bookRepository, times(1)).saveAndFlush(book1);
    }
}