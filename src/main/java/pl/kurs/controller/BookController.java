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
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.model.dto.BookDto;
import pl.kurs.service.BookService;

@RestController
@RequestMapping("api/v1/books")
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostConstruct
    public void init() {
        bookService.init();
    }

    @GetMapping
    public ResponseEntity<Page<BookDto>> findAll(@PageableDefault Pageable pageable) {
        log.info("findAll");
        return ResponseEntity.ok(bookService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<BookDto> addBook(@RequestBody CreateBookCommand command) {
        log.info("addBook({})", command);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.addBook(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDto> findBook(@PathVariable int id) {
        log.info("findBook({})", id);
        return ResponseEntity.ok(bookService.findBook(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookDto> deleteBook(@PathVariable int id) {
        log.info("deleteBook({})", id);
        bookService.deleteBook(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDto> editBook(@PathVariable int id, @RequestBody EditBookCommand command) {
        log.info("editBook({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(bookService.editBook(id, command));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookDto> editBookPartially(@PathVariable int id, @RequestBody EditBookCommand command) {
        log.info("editBook({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(bookService.editBookPartially(id, command));
    }
}
