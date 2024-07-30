package pl.kurs.service;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final EntityManager entityManager;


    @PostConstruct
    public void init() {
        Author a1 = authorRepository.saveAndFlush(new Author("Kazimierz", "Wielki", 1900, 2000));
        Author a2 = authorRepository.saveAndFlush(new Author("Maria", "Wielka", 1900, 2000));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem", "LEKTURA", true, a1));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 2", "LEKTURA", true, a2));
    }

    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(BookDto::toDto);
    }

    @Transactional
    public BookDto addBook(CreateBookCommand command) {
        Author author = authorRepository.findById(command.getAuthorId()).orElseThrow(AuthorNotFoundException::new);
        Book book = bookRepository.save(new Book(command.getTitle(), command.getCategory(), true, author));
        return BookDto.toDto(book);
    }


    public BookDto findBook(int id) {
        return BookDto.toDto(bookRepository.findById(id).orElseThrow(BookNotFoundException::new));
    }

    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    public BookDto editBook(int id, EditBookCommand command) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        book.setAvailable(command.getAvailable());
        book.setCategory(command.getCategory());
        book.setTitle(command.getTitle());
        return BookDto.toDto(bookRepository.saveAndFlush(book));
    }

    public BookDto editBookPartially(int id, EditBookCommand command) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        Optional.ofNullable(command.getAvailable()).ifPresent(book::setAvailable);
        Optional.ofNullable(command.getCategory()).ifPresent(book::setCategory);
        Optional.ofNullable(command.getTitle()).ifPresent(book::setTitle);
        return BookDto.toDto(bookRepository.saveAndFlush(book));
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