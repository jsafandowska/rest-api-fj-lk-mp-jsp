package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.exceptions.BookNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.model.dto.BookDto;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(BookDto::toDto);
    }

    public BookDto addBook(CreateBookCommand command) {
        Author author = authorRepository.findById(command.getAuthorId()).orElseThrow(AuthorNotFoundException::new);
        Book book = new Book(command.getTitle(), command.getCategory(), true, author);
        book = bookRepository.saveAndFlush(book);
        return BookDto.toDto(book);
    }

    public BookDto findBook(int id) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        return BookDto.toDto(book);
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    public BookDto editBook(int id, EditBookCommand command) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        book.setTitle(command.getTitle());
        book.setCategory(command.getCategory());
        book.setAvailable(command.getAvailable());
        book = bookRepository.saveAndFlush(book);
        return BookDto.toDto(book);
    }

    public BookDto editBookPartially(int id, EditBookCommand command) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        Optional.ofNullable(command.getTitle()).ifPresent(book::setTitle);
        Optional.ofNullable(command.getCategory()).ifPresent(book::setCategory);
        Optional.ofNullable(command.getAvailable()).ifPresent(book::setAvailable);
        book = bookRepository.saveAndFlush(book);
        return BookDto.toDto(book);
    }
}

