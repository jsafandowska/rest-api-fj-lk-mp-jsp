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
        private Author authorWithBooks;
        private CreateAuthorCommand createAuthorCommand;
        private EditAuthorCommand editAuthorCommand;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            author = new Author("John", "Doe", 1970, 2020);
            authorWithBooks = new Author("Jane", "Doe", 1980, 2021);
            createAuthorCommand = new CreateAuthorCommand("Jane", "Doe", 1980, 2021);
            editAuthorCommand = new EditAuthorCommand(null, "Doe", 1980, null);
        }

       @Test
       public void shouldReturnAllAuthorsWithBooks() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Author> authorsPage = new PageImpl<>(List.of(authorWithBooks), pageable, 1);
            when(authorRepository.findAllWithBooks(pageable)).thenReturn(authorsPage);
            Page<Author> result = authorService.getAllAuthorsWithBooks(pageable);
            assertEquals(1, result.getTotalElements());
            assertEquals("Jane", result.getContent().get(0).getName());
            verify(authorRepository, times(1)).findAllWithBooks(pageable);
        }

        @Test
      public void shouldAddAuthor() {
            when(authorRepository.saveAndFlush(any(Author.class))).thenReturn(author);
            Author result = authorService.addAuthor(new CreateAuthorCommand("John","Doe",1970,2020));
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
            when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));
            when(authorRepository.saveAndFlush(any(Author.class))).thenReturn(author);
            Author result = authorService.editAuthor(1, createAuthorCommand);
            assertEquals("Jane", result.getName());
            assertEquals("Doe", result.getSurname());
            assertEquals(1980, result.getBirthYear());
            assertEquals(2021, result.getDeathYear());
            verify(authorRepository, times(1)).findById(1);
            verify(authorRepository, times(1)).saveAndFlush(any(Author.class));
        }

        @Test
        void shouldEditAuthorPartially() {
            when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));
            when(authorRepository.saveAndFlush(any(Author.class))).thenReturn(author);
            Author result = authorService.editAuthorPartially(1, editAuthorCommand);
            assertEquals("John", result.getName());
            assertEquals("Doe", result.getSurname());
            assertEquals(1980, result.getBirthYear());
            assertEquals(2020, result.getDeathYear());
            verify(authorRepository, times(1)).findById(1);
            verify(authorRepository, times(1)).saveAndFlush(any(Author.class));
        }
    }

