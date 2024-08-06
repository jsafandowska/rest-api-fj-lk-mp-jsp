package pl.kurs.inheritance.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonFacadeFactory {

    private final Map<String, PersonFacade> facades;

    @Autowired
    public PersonFacadeFactory(List<PersonFacade> facades) {
        this.facades = facades.stream()
                .collect(Collectors.toMap(
                        f -> f.getClass().getSimpleName().replace("Facade", "").toUpperCase(),
                        f -> f
                ));
    }

    public PersonFacade getFacade(String type) {
        return facades.get(type.toUpperCase());
    }
}