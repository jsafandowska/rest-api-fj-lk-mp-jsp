package pl.kurs.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.model.dto.BookDto;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;
    @InjectMocks
    private BookService bookService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnAllBooks() {
        Pageable pageable = PageRequest.of(0, 2);
        Book book1 = new Book("Ogniem i mieczem", "LEKTURA", true, new Author());
        Book book2 = new Book("Ogniem i mieczem 2", "LEKTURA", true, new Author());
        Page<Book> page = new PageImpl<>(Arrays.asList(book1, book2), pageable, 2);
        when(bookRepository.findAll(pageable)).thenReturn(page);
        Page<BookDto> result = bookService.findAll(pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals("Ogniem i mieczem", result.getContent().get(0).title());
        assertEquals("Ogniem i mieczem 2", result.getContent().get(1).title());
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    public void shouldAddBook() {
        Author author = new Author("Kazimierz", "Wielki", 1900, 2000);
        Book book = new Book("New Book", "CATEGORY", true, author);
        CreateBookCommand command = new CreateBookCommand("New Book", "CATEGORY", 1);
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book);
        BookDto result = bookService.addBook(command);
        assertEquals("New Book", result.title());
        verify(bookRepository).saveAndFlush(any(Book.class));
    }


    @Test
    public void shouldFindBook() {
        Author author = new Author("Henryk", "Sienkiewicz", 1846, 1916);
        Book book = new Book("Ogniem i mieczem", "LEKTURA", true, author);
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        BookDto result = bookService.findBook(1);
        assertEquals("Ogniem i mieczem", result.title());
        verify(bookRepository, times(1)).findById(1);
    }

    @Test
    public void shouldDeleteBook() {
        bookService.deleteBook(1);
        verify(bookRepository,times(1)).deleteById(1);
    }

    @Test
    public void shouldEditBook() {
        Book book = new Book("Ogniem i mieczem", "LEKTURA", true, new Author());
        EditBookCommand command = new EditBookCommand("New Title", "New Category", false);
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book);
        BookDto result = bookService.editBook(1, command);
        assertEquals("New Title", result.title());
        verify(bookRepository,times(1)).findById(1);
        verify(bookRepository,times(1)).saveAndFlush(book);
    }

    @Test
    public void shouldEditBookPartially() {
        Book book = new Book("Ogniem i mieczem", "LEKTURA", true, new Author());
        EditBookCommand command = new EditBookCommand(null, "New Category", false);
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book);
        BookDto result = bookService.editBookPartially(1, command);
        assertEquals("Ogniem i mieczem", result.title());
        assertEquals("New Category", result.category());
        verify(bookRepository,times(1)).findById(1);
        verify(bookRepository,times(1)).saveAndFlush(book);
    }
}