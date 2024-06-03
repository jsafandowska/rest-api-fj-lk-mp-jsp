package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.BookNotFoundException;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/books")
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    @PostConstruct
    public void init() {
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem", "LEKTURA", true));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 2", "LEKTURA", true));
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        log.info("findAll");
        return ResponseEntity.ok(bookRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody CreateBookCommand command) {
        log.info("addBook({})", command);
        Book book = bookRepository.saveAndFlush(new Book(command.getTitle(), command.getCategory(), true));
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findBook(@PathVariable int id) {
        log.info("findBook({})", id);
        return ResponseEntity.ok(bookRepository.findById(id).orElseThrow(BookNotFoundException::new));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable int id) {
        log.info("deleteBook({})", id);
        bookRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> editBook(@PathVariable int id, @RequestBody EditBookCommand command) {
        log.info("editBook({}, {})", id, command);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        book.setAvailable(command.getAvailable());
        book.setCategory(command.getCategory());
        book.setTitle(command.getTitle());
        return ResponseEntity.status(HttpStatus.OK).body(bookRepository.saveAndFlush(book));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> editBookPartially(@PathVariable int id, @RequestBody EditBookCommand command) {
        log.info("editBook({}, {})", id, command);
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        Optional.ofNullable(command.getAvailable()).ifPresent(book::setAvailable);
        Optional.ofNullable(command.getCategory()).ifPresent(book::setCategory);
        Optional.ofNullable(command.getTitle()).ifPresent(book::setTitle);
        return ResponseEntity.status(HttpStatus.OK).body(bookRepository.saveAndFlush(book));
    }


}
