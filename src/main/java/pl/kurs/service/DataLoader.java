package pl.kurs.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.dictionary.repository.DictionaryRepository;
import pl.kurs.inheritance.enums.Gender;
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

import java.time.LocalDate;
import java.util.List;

//@Service
//@RequiredArgsConstructor
//@Profile("no-liquibase")
//public class DataLoader {
//
//    private final CarRepository carRepository;
//    private final PersonRepository personRepository;
//    private final GarageRepository garageRepository;
//    private final BookRepository bookRepository;
//    private final AuthorRepository authorRepository;
//    private final DictionaryRepository dictionaryRepository;
//
//    @PostConstruct
//    public void init() {
//
//        Author a1 = authorRepository.saveAndFlush(new Author("Kazimierz", "Wielki", 1900, 2000));
//        Author a2 = authorRepository.saveAndFlush(new Author("Maria", "Wielka", 1900, 2000));
//        Author a3 = authorRepository.saveAndFlush(new Author("Marcin", "Jakis", 2000, null));
//        bookRepository.saveAndFlush(new Book("Ogniem i mieczem ", "KOMEDIA", true, a1));
//        bookRepository.saveAndFlush(new Book("w pustyni i w puszczy", "LEKTURA", true, a1));
//        bookRepository.saveAndFlush(new Book("potop", "KOMEDIA", true, a1));
//        bookRepository.saveAndFlush(new Book("potop 2", "LEKTURA", true, a1));
//        bookRepository.saveAndFlush(new Book("Ogniem i mieczem 1", "KRYMINAŁ", true, a1));
//        bookRepository.saveAndFlush(new Book("Pan Wołodyjowski", "LEKTURA", true, a2));
//        bookRepository.saveAndFlush(new Book("Krzyzacy", "KRYMINAŁ", false, a2));
//        bookRepository.saveAndFlush(new Book("Dziady", "LEKTURA", false, a3));
//        bookRepository.saveAndFlush(new Book("Przedwiosnie", "ROMANS", true, a3));
//        bookRepository.saveAndFlush(new Book("zboze", "LEKTURA", true, a3));
//
//
//        garageRepository.saveAndFlush(new Garage(2, "Warszawa", true));
//        garageRepository.saveAndFlush(new Garage(3, "Piątkowska", false));
//        garageRepository.saveAndFlush(new Garage(3, "Piątkowska", false));
//        carRepository.saveAndFlush(new Car("Mercedes", "S-class", "petrol"));
//        carRepository.saveAndFlush(new Car("Audi", "RS", "petrol"));
//
//        Dictionary countries = new Dictionary("COUNTRIES");
//        Dictionary positions = new Dictionary("POSITIONS");
//
//        new DictionaryValue("Polska", countries);
//        new DictionaryValue("Niemcy", countries);
//        new DictionaryValue("Wielka Brytania", countries);
//        new DictionaryValue("Francja", countries);
//        new DictionaryValue("Devops", positions);
//        new DictionaryValue("Senior Developer", positions);
//        new DictionaryValue("Mid Developer", positions);
//        new DictionaryValue("Junior Developer", positions);
//        dictionaryRepository.saveAllAndFlush(List.of(countries, positions));
//
//
//        DictionaryValue france = countries.getValueByName("Francja");
//        DictionaryValue devops = positions.getValueByName("Devops");
//
//        List<Employee> employees = List.of(
//                new Employee("X", 25, LocalDate.of(1999, 1, 1), Gender.MALE, france, devops, 15000),
//                new Employee("Y", 45, LocalDate.of(1979, 1, 1), Gender.MALE, france, devops, 25000),
//                new Employee("Z", 35, LocalDate.of(1989, 1, 1), Gender.FEMALE, france, devops, 17000)
//        );
//
//        List<Student> students = List.of(
//                new Student("A", 20, LocalDate.of(2004, 1, 1), Gender.FEMALE, france, 1000, "1a"),
//                new Student("B", 19, LocalDate.of(2005, 1, 1), Gender.MALE, france, 0, "2a"),
//                new Student("C", 21, LocalDate.of(2003, 1, 1), Gender.FEMALE, france, 500, "1b")
//        );
//
//        personRepository.saveAllAndFlush(employees);
//        personRepository.saveAllAndFlush(students);
//    }
//}
