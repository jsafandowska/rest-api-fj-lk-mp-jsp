package pl.kurs.service;
import jakarta.annotation.PostConstruct;
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
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;


    @PostConstruct
    public void init() {
        Author a1 = authorRepository.saveAndFlush(new Author("Kazimierz", "Wielki", 1900, 2000));
        Author a2 = authorRepository.saveAndFlush(new Author("Maria", "Wielka", 1900, 2000));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem", "LEKTURA", true, a1));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 2", "LEKTURA", true, a2));
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

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

    public Book editBook(int id, EditBookCommand command) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        book.setAvailable(command.getAvailable());
        book.setCategory(command.getCategory());
        book.setTitle(command.getTitle());
        return bookRepository.saveAndFlush(book);
    }

    public Book editBookPartially(int id, EditBookCommand command) {
        Book book = bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        Optional.ofNullable(command.getAvailable()).ifPresent(book::setAvailable);
        Optional.ofNullable(command.getCategory()).ifPresent(book::setCategory);
        Optional.ofNullable(command.getTitle()).ifPresent(book::setTitle);
        return bookRepository.saveAndFlush(book);
    }

}
