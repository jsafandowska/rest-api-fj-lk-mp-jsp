package pl.kurs.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.exceptions.BookNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.model.dto.BookDto;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
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

    public void importBooks(byte[] bytes) {
        String content = new String(bytes, Charset.defaultCharset());

        AtomicInteger counter = new AtomicInteger(0);
        AtomicLong start = new AtomicLong(System.currentTimeMillis());

        Arrays.stream(content.split("\r\n"))
                .map(line -> line.split(","))
                .map(args -> new CreateBookCommand(args))
                .peek(command -> countTime(counter, start))
                .forEach(this::addBook);
    }

    private void countTime(AtomicInteger counter, AtomicLong start) {
        if (counter.incrementAndGet() % 1000 == 0) {
            log.info("Imported: {} in {} ms", counter, (System.currentTimeMillis() - start.get()));
            start.set(System.currentTimeMillis());
        }
    }

//    20-40k na sekunde
//    minimalne zuzycie pamieci
    // pobawic sie z visual vm

        @Async("taskExecutor")
    public CompletableFuture<Void> importBooksAsync(MultipartFile file) throws IOException {
        List<CreateBookCommand> batch = new ArrayList<>();
        int batchSize = 100;
        AtomicInteger counter = new AtomicInteger(0);
        AtomicLong start = new AtomicLong(System.currentTimeMillis());

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookData = line.split(",");
                CreateBookCommand command = new CreateBookCommand(bookData[0], bookData[1], Integer.parseInt(bookData[2]));
                batch.add(command);
                countTime(counter,start);
                if (batch.size() >= batchSize) {
                    addBooks(batch);
                    batch.clear();

                }
            }

            if (!batch.isEmpty()) {
                addBooks(batch);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.completedFuture(null);
    }
//    @Async("taskExecutor")
//    public CompletableFuture<Void> importBooksAsync(MultipartFile file) throws IOException {
////        List<CreateBookCommand> batch = new ArrayList<>();
////        int batchSize = 100;
//        AtomicInteger counter = new AtomicInteger(0);
//        AtomicLong start = new AtomicLong(System.currentTimeMillis());
//
//        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] bookData = line.split(",");
//                CreateBookCommand command = new CreateBookCommand(bookData[0], bookData[1], Integer.parseInt(bookData[2]));
//                addBook(command);
//                countTime(counter, start);
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return CompletableFuture.completedFuture(null);
//    }

    public void addBooks(List<CreateBookCommand> commands) {
        for (CreateBookCommand command : commands) {
            addBook(command);
        }
    }


}
