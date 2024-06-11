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
import pl.kurs.model.Author;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;
import pl.kurs.model.dto.AuthorDto;
import pl.kurs.repository.AuthorRepository;

import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;

    @BeforeEach
    public void setUp() {
        author = new Author("John", "Doe", 1900, 2000);
        author.setId(1);
    }

    @Test
    public void testFindAll() {
        when(authorRepository.findAll()).thenReturn(List.of(author));

        List<AuthorDto> result = authorService.findAll();

        assertEquals(1, result.size());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    public void testAddAuthor() {
        CreateAuthorCommand command = new CreateAuthorCommand("New", "Author", 1900, 2000);
        when(authorRepository.saveAndFlush(any(Author.class))).thenReturn(author);

        AuthorDto result = authorService.addAuthor(command);

        assertEquals(author.getId(), result.id());
        verify(authorRepository, times(1)).saveAndFlush(any(Author.class));
    }

    @Test
    public void testFindAuthor() {
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));

        AuthorDto result = authorService.findAuthor(1);

        assertEquals(author.getId(), result.id());
        verify(authorRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testDeleteAuthor() {
        doNothing().when(authorRepository).deleteById(anyInt());

        authorService.deleteAuthor(1);

        verify(authorRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void testEditAuthor() {
        EditAuthorCommand command = new EditAuthorCommand("Updated", "Author", 1900, 2000);
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));
        when(authorRepository.saveAndFlush(any(Author.class))).thenReturn(author);

        AuthorDto result = authorService.editAuthor(1, command);

        assertEquals("Updated", result.name());
        assertEquals("Author", result.surname());
        verify(authorRepository, times(1)).findById(anyInt());
        verify(authorRepository, times(1)).saveAndFlush(any(Author.class));
    }

    @Test
    public void testEditAuthorPartially() {
        EditAuthorCommand command = new EditAuthorCommand(null, "Updated", null, null);
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));
        when(authorRepository.saveAndFlush(any(Author.class))).thenReturn(author);

        AuthorDto result = authorService.editAuthorPartially(1, command);

        assertEquals(author.getName(), result.name());
        assertEquals("Updated", result.surname());
        verify(authorRepository, times(1)).findById(anyInt());
        verify(authorRepository, times(1)).saveAndFlush(any(Author.class));
    }
}
