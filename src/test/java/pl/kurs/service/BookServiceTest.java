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
import pl.kurs.model.Garage;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.model.dto.BookDto;
import pl.kurs.model.dto.GarageDto;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    public void init() {
        MockitoAnnotations.openMocks(this);
        author = new Author("Kazimierz", "Wielki", 1900, 2000);
        book1 = new Book("Ogniem i mieczem", "LEKTURA", true, author);
        book2 = new Book("Ogniem i mieczem 2", "LEKTURA", true, author);

    }

    @Test
    public void shouldReturnAllBooks() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Book> page = new PageImpl<>(Arrays.asList(book1, book2), pageable, 2);
        when(bookRepository.findAll(pageable)).thenReturn(page);
        Page<BookDto> result = bookService.findAll(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(book1.getId(), result.getContent().get(0).id());
        assertEquals(book2.getId(), result.getContent().get(1).id());
    }

    @Test
    public void shouldAddBook() {

        CreateBookCommand createBookCommand = new CreateBookCommand("Ogniem i mieczem3", "LEKTURA", 1);
        Book book = new Book(createBookCommand.getTitle(), createBookCommand.getCategory(), true, author);
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book);
        BookDto bookDto = bookService.addBook(createBookCommand);
        assertEquals("Ogniem i mieczem3", bookDto.title());
        assertEquals("LEKTURA", bookDto.category());
    }
    @Test
    public void shouldFindBook(){
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book1));
        BookDto bookDto = bookService.findBook(book1.getId());
        assertEquals("Ogniem i mieczem", bookDto.title());
        assertEquals("LEKTURA", bookDto.category());

    }
    @Test
    public void shouldDeleteBook(){
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1,book2));
        bookService.deleteBook(1);
        verify(bookRepository).deleteById(1);
    }
    @Test
    public void shouldEditBook(){
        EditBookCommand editBookCommand = new EditBookCommand("Ogniem i mieczem3", "LEKTURA", true);
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book1));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book1);
        BookDto bookDto = bookService.editBook(1, editBookCommand);
        assertEquals("Ogniem i mieczem3", bookDto.title());
        assertEquals("LEKTURA", bookDto.category());

    }
    @Test
    public void shouldEditBookPartially(){
        EditBookCommand editBookCommand = new EditBookCommand("Ogniem i mieczem3", null, null);
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book1));
        when(bookRepository.saveAndFlush(any(Book.class))).thenReturn(book1);
        BookDto bookDto = bookService.editBookPartially(1, editBookCommand);
        assertEquals("Ogniem i mieczem3", bookDto.title());
        assertEquals("LEKTURA", bookDto.category());


    }

}