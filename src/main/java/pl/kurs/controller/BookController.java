package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.BookNotFoundException;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.service.BookIdGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("api/v1/books")
//swego rodzaju ścieżka do kontrolera
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final List<Book> books;

    private final BookIdGenerator bookIdGenerator;

//    @PostConstruct
//    public void init() {
//        books.add(new Book(bookIdGenerator.getId(), "Ogniem i mieczem", "LEKTURA", true));
//        books.add(new Book(bookIdGenerator.getId(), "Ogniem i mieczem 2", "LEKTURA", true));
//    }
//    te dane są zapisywane na sam koniec

    @GetMapping
    public ResponseEntity<List<Book>> findAll() {
        log.info("findAll");
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody CreateBookCommand command) {
        log.info("addBook({})", command);
        Book book = new Book(bookIdGenerator.getId(), command.getTitle(), command.getCategory(), true);
        books.add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> findBook(@PathVariable int id) {
        log.info("findBook({})", id);
        return ResponseEntity.ok(books.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(BookNotFoundException::new));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Book> deleteBook(@PathVariable int id) {
        log.info("deleteBook({})", id);
        if (!books.removeIf(b -> b.getId() == id)) {
            throw new BookNotFoundException();
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> editBook(@PathVariable int id, @RequestBody EditBookCommand command) {
        log.info("editBook({}, {})", id, command);
        Book book = books.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(BookNotFoundException::new);
        book.setAvailable(command.getAvailable());
        book.setCategory(command.getCategory());
        book.setTitle(command.getTitle());
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> editBookPartially(@PathVariable int id, @RequestBody EditBookCommand command) {
        log.info("editBook({}, {})", id, command);
        Book book = books.stream().filter(b -> b.getId() == id).findFirst().orElseThrow(BookNotFoundException::new);
        Optional.ofNullable(command.getAvailable()).ifPresent(book::setAvailable);
        Optional.ofNullable(command.getCategory()).ifPresent(book::setCategory);
        Optional.ofNullable(command.getTitle()).ifPresent(book::setTitle);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }


}
