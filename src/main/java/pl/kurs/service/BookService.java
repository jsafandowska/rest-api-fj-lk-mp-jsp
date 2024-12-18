package pl.kurs.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.exceptions.BookNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.model.dto.BookDto;
import pl.kurs.model.query.FindBookQuery;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    public Page<Book> findAll(Pageable pageable, FindBookQuery query) {
        return bookRepository.findAll(query.toPredicate(), pageable);
    }

    @Transactional
    public Book addBook(CreateBookCommand command) {
        Author author = authorRepository.findById(command.getAuthorId()).orElseThrow(AuthorNotFoundException::new);
        return bookRepository.saveAndFlush(new Book(command.getTitle(), command.getCategory(), true, author));
    }


    public Book findBook(int id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public Book editBook(int id, EditBookCommand command) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        Book copy = new Book(book);
        copy.setAvailable(command.getAvailable());
        copy.setCategory(command.getCategory());
        copy.setTitle(command.getTitle());
        copy.setVersion(command.getVersion());
        return bookRepository.saveAndFlush(copy);
    }

    public Book editBookPartially(int id, EditBookCommand command) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        Optional.ofNullable(command.getAvailable()).ifPresent(book::setAvailable);
        Optional.ofNullable(command.getCategory()).ifPresent(book::setCategory);
        Optional.ofNullable(command.getTitle()).ifPresent(book::setTitle);
        return bookRepository.saveAndFlush(book);
    }

    public Book save(CreateBookCommand command) {
        Author author = authorRepository.findById(command.getAuthorId()).orElseThrow(AuthorNotFoundException::new);
        return bookRepository.save(new Book(command.getTitle(), command.getCategory(), true, author));
    }

    @Transactional
    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }


//    public void importBooks(byte[] bytes) {
//        String content = new String(bytes, Charset.defaultCharset());
//        AtomicInteger counter = new AtomicInteger(0);
//        AtomicLong start = new AtomicLong(System.currentTimeMillis());
//        Arrays.stream(content.split("\r\n"))
//                .map(line -> line.split(","))
//                .map(args -> new CreateBookCommand(args))
//                .peek(command -> countTime(counter, start))
//                .forEach(this::addBook);
//    }

}
//    20-40k na sekunde
//    minimalne zuzycie pamieci
// pobawic sie z visual vm


