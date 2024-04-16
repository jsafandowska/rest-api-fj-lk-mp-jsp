package pl.kurs.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.model.Book;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    private List<Book> books = new ArrayList<>();

    @PostConstruct
    public void init(){
        books.add(new Book(1, "Ogniem i mieczem", "LEKTURA", true));
        books.add(new Book(2, "Ogniem i mieczem 2", "LEKTURA", true));
    }

    @GetMapping
    public ResponseEntity<List<Book>> findAll(){
        return ResponseEntity.ok(books);
    }


}
