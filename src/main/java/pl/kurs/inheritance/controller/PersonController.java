package pl.kurs.inheritance.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.inheritance.model.Employee;
import pl.kurs.inheritance.model.Person;
import pl.kurs.inheritance.model.Student;
import pl.kurs.inheritance.repository.PersonRepository;

import java.util.List;

@RestController
@RequestMapping("/api/v1/people")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository personRepository;

    @PostConstruct
    public void init(){
        personRepository.saveAllAndFlush(
                List.of(
                        new Employee("X", 25, "programmer", 15000),
                        new Employee("Y", 45, "programmer", 25000),
                        new Employee("Z", 35, "programmer", 17000),
                        new Student("A", 20, 1000, "1a"),
                        new Student("B", 19, 0, "2a"),
                        new Student("C", 21, 500, "1b")
                )
        );
    }

    @GetMapping
    public List<Person> findAll(){
        return personRepository.findAll();
    }


}
