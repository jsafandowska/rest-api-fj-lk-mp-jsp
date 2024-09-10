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
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;

import pl.kurs.model.dto.FullAuthorDto;
import pl.kurs.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;

    private FullAuthorDto fullAuthorDto;
    private FullAuthorDto fullAuthorDto2;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        author = new Author("John", "Doe", 1970, 2020);
        fullAuthorDto = new FullAuthorDto(1, "Jane", "Doe", 1980, null, 2L);
        fullAuthorDto2 = new FullAuthorDto(1, "Jane2", "Doe", 1980, null, 3L);
    }

    //to do test do poprawy
    @Test
    public void shouldReturnAllAuthorsWithBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<FullAuthorDto> authorsPage = new PageImpl<>(List.of(fullAuthorDto, fullAuthorDto2), pageable, 1);
        when(authorRepository.findAllWithBooks(pageable)).thenReturn(authorsPage);
        Page<FullAuthorDto> result = authorService.getAllAuthorsWithBooks(pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals("Jane", result.getContent().get(0).name());
        assertEquals("Jane2", result.getContent().get(1).name());
        assertEquals(2L, result.getContent().get(0).amountOfBooks());
        assertEquals(3L, result.getContent().get(1).amountOfBooks());
        verify(authorRepository, times(1)).findAllWithBooks(pageable);
    }

    @Test
    public void shouldAddAuthor() {
        when(authorRepository.saveAndFlush(any(Author.class))).thenReturn(author);
        Author result = authorService.addAuthor(new CreateAuthorCommand("John", "Doe", 1970, 2020));
        assertEquals("John", result.getName());
        verify(authorRepository, times(1)).saveAndFlush(any(Author.class));
    }

    @Test
    void shouldFindAuthor() {
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));
        Author result = authorService.findAuthor(1);
        assertEquals("John", result.getName());
        verify(authorRepository, times(1)).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFound() {
        when(authorRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(AuthorNotFoundException.class, () -> authorService.findAuthor(1));
    }

    @Test
    void shouldDeleteAuthor() {
        doNothing().when(authorRepository).deleteById(anyInt());
        authorService.deleteAuthor(1);
        verify(authorRepository, times(1)).deleteById(1);
    }

    @Test
    void shouldEditAuthor() {
        EditAuthorCommand editAuthorCommand = new EditAuthorCommand("John2", "Doe2", 2000, 0L);
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));
        when(authorRepository.saveAndFlush(any(Author.class))).thenReturn(new Author("John2", "Doe2", 1970, 2000));
        Author result = authorService.editAuthor(1, editAuthorCommand);
        assertEquals("John2", result.getName());
        assertEquals("Doe2", result.getSurname());
        assertEquals(1970, result.getBirthYear());
        assertEquals(2000, result.getDeathYear());
        verify(authorRepository, times(1)).findById(1);
        verify(authorRepository, times(1)).saveAndFlush(any(Author.class));
    }

    @Test
    void shouldEditAuthorPartially() {
        EditAuthorCommand editAuthorCommand = new EditAuthorCommand(null, "Doe2", null, 0L);
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));
        when(authorRepository.saveAndFlush(any(Author.class))).thenReturn(author);
        Author result = authorService.editAuthorPartially(1, editAuthorCommand);
        assertEquals("John", result.getName());
        assertEquals("Doe2", result.getSurname());
        assertEquals(1970, result.getBirthYear());
        assertEquals(2020, result.getDeathYear());
        verify(authorRepository, times(1)).findById(1);
        verify(authorRepository, times(1)).saveAndFlush(any(Author.class));
    }
}