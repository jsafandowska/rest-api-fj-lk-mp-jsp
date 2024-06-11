package pl.kurs.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    private Author author;
    private Book book;

    @BeforeEach
    public void setUp() {
        author = new Author("John", "Doe", 1900, 2000);
        author.setId(1);

        book = new Book("Sample Book", "Category", true, author);
        book.setId(1);
    }

    @Test
    public void testFindAll() {
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(book)));

        Page<BookDto> result = bookService.findAll(PageRequest.of(0, 10));

        assertEquals(1, result.getContent().size());
        verify(bookRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testAddBook() {
        CreateBookCommand command = new CreateBookCommand("New Book", "New Category", 1);
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book);

        BookDto result = bookService.addBook(command);

        assertEquals(book.getId(), result.id());
        verify(authorRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).saveAndFlush(any(Book.class));
    }

    @Test
    public void testFindBook() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        BookDto result = bookService.findBook(1);

        assertEquals(book.getId(), result.id());
        verify(bookRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testDeleteBook() {
        doNothing().when(bookRepository).deleteById(anyInt());

        bookService.deleteBook(1);

        verify(bookRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void testEditBook() {
        EditBookCommand command = new EditBookCommand("Updated Title", "Updated Category", false);
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book);

        BookDto result = bookService.editBook(1, command);

        assertEquals("Updated Title", result.title());
        assertEquals("Updated Category", result.category());
        assertFalse(result.available());
        verify(bookRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).saveAndFlush(any(Book.class));
    }

    @Test
    public void testEditBookPartially() {
        EditBookCommand command = new EditBookCommand(null, "Updated Category", null);
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book);

        BookDto result = bookService.editBookPartially(1, command);

        assertEquals(book.getTitle(), result.title());
        assertEquals("Updated Category", result.category());
        assertTrue(result.available());
        verify(bookRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).saveAndFlush(any(Book.class));
    }
}
