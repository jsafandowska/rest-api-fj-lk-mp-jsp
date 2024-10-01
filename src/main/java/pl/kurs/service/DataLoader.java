package pl.kurs.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.dictionary.repository.DictionaryRepository;
import pl.kurs.inheritance.model.Employee;
import pl.kurs.inheritance.model.Student;
import pl.kurs.inheritance.repository.PersonRepository;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.repository.AuthorRepository;
import pl.kurs.repository.BookRepository;
import pl.kurs.repository.CarRepository;
import pl.kurs.repository.GarageRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Profile("no-liquibase")
public class DataLoader {

    private final CarRepository carRepository;
    private final PersonRepository personRepository;
    private final GarageRepository garageRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final DictionaryRepository dictionaryRepository;

    @PostConstruct
    public void init() {
//        personRepository.saveAllAndFlush(
//                List.of(
//                        new Employee("X", 25, "programmer", 15000),
//                        new Employee("Y", 45, "programmer", 25000),
//                        new Employee("Z", 35,  "programmer", 17000),
//                        new Student("A", 20, 1000, "1a"),
//                        new Student("B", 19,  0, "2a"),
//                        new Student("C", 21,  500, "1b")
//                )
//        );
        Author a1 = authorRepository.saveAndFlush(new Author("Kazimierz", "Wielki", 1900, 2000));
        Author a2 = authorRepository.saveAndFlush(new Author("Maria", "Wielka", 1900, 2000));
        Author a3 = authorRepository.saveAndFlush(new Author("Marcin", "Jakis", 2000, null));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem", "LEKTURA", true, a1));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 2", "LEKTURA", true, a1));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 3", "LEKTURA", true, a1));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 4", "LEKTURA", true, a1));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 5", "LEKTURA", true, a1));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 6", "LEKTURA", true, a2));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 7", "LEKTURA", true, a2));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 8", "LEKTURA", true, a3));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 9", "LEKTURA", true, a3));
        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 10", "LEKTURA", true, a3));
        garageRepository.saveAndFlush(new Garage(2, "Warszawa", true));
        garageRepository.saveAndFlush(new Garage(3, "Piątkowska", false));
        garageRepository.saveAndFlush(new Garage(3, "Piątkowska", false));
        carRepository.saveAndFlush(new Car("Mercedes", "S-class", "petrol"));
        carRepository.saveAndFlush(new Car("Audi", "RS", "petrol"));

        Dictionary countries = new Dictionary("COUNTRIES");
        Dictionary positions = new Dictionary("POSITIONS");

        new DictionaryValue("Polska", countries);
        new DictionaryValue("Niemcy", countries);
        new DictionaryValue("Wielka Brytania", countries);
        new DictionaryValue("Francja", countries);
        new DictionaryValue("Devops", positions);
        new DictionaryValue("Senior Developer", positions);
        new DictionaryValue("Mid Developer", positions);
        new DictionaryValue("Junior Developer", positions);


        dictionaryRepository.saveAllAndFlush(List.of(countries, positions));

    }
}
