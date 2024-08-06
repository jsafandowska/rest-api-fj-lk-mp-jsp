package pl.kurs.inheritance.model.factory;

import pl.kurs.inheritance.model.Person;
import pl.kurs.inheritance.model.command.CreatePersonCommand;

public interface PersonFactory {
    Person create(CreatePersonCommand command);
}
