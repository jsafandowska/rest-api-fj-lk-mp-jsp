package pl.kurs.inheritance.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.kurs.inheritance.dto.PersonDto;
import pl.kurs.inheritance.model.command.CreatePersonCommand;
import pl.kurs.inheritance.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/people")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PersonDto> findAll(){
        return personService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDto addPerson(@RequestBody CreatePersonCommand command) {
        return personService.createPerson(command);
    }

}
