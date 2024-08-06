package pl.kurs.inheritance.facade;
import pl.kurs.inheritance.dto.PersonDto;
import pl.kurs.inheritance.model.PersonParameter;
import java.util.List;

public interface PersonFacade {
    PersonDto addPerson(List<PersonParameter> parameters);
}