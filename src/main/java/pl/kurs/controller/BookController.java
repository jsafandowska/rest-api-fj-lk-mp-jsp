package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.exceptions.BookNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.model.dto.BookDto;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/books")
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @PostConstruct
    public void init() {
        Author a1 = authorRepository.saveAndFlush(new Author("Kazimierz", "Wielki", 1900, 2000));
        Author a2 = authorRepository.saveAndFlush(new Author("Maria", "Wielka", 1900, 2000));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem", "LEKTURA", true, a1));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 2", "LEKTURA", true, a2));
    }

    @GetMapping
    public ResponseEntity<Page<BookDto>> findAll(@PageableDefault Pageable pageable) {
        log.info("findAll");
        return ResponseEntity.ok(bookRepository.findAll(pageable).map(BookDto::toDto));
    }

    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody CreateBookCommand command) {
        log.info("addBook({})", command);
        Author author = authorRepository.findById(command.getAuthorId()).orElseThrow(AuthorNotFoundException::new);
        Book book = bookRepository.saveAndFlush(new Book(command.getTitle(), command.getCategory(), true, author));
        return ResponseEntity.status(HttpStatus.CREATED).body(BookDto.toDto(book));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findBook(@PathVariable int id) {
        log.info("findBook({})", id);
        return ResponseEntity.ok(BookDto.toDto(bookRepository.findById(id).orElseThrow(BookNotFoundException::new)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable int id) {
        log.info("deleteBook({})", id);
        bookRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> editBook(@PathVariable int id, @RequestBody EditBookCommand command) {
        log.info("editBook({}, {})", id, command);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        book.setAvailable(command.getAvailable());
        book.setCategory(command.getCategory());
        book.setTitle(command.getTitle());
        return ResponseEntity.status(HttpStatus.OK).body(BookDto.toDto(bookRepository.saveAndFlush(book)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> editBookPartially(@PathVariable int id, @RequestBody EditBookCommand command) {
        log.info("editBook({}, {})", id, command);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        Optional.ofNullable(command.getAvailable()).ifPresent(book::setAvailable);
        Optional.ofNullable(command.getCategory()).ifPresent(book::setCategory);
        Optional.ofNullable(command.getTitle()).ifPresent(book::setTitle);
        return ResponseEntity.status(HttpStatus.OK).body(BookDto.toDto(bookRepository.saveAndFlush(book)));
    }

}
