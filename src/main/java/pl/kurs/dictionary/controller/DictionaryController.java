package pl.kurs.dictionary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.command.CreateDictionaryCommand;
import pl.kurs.dictionary.model.dto.DictionaryDto;
import pl.kurs.dictionary.service.DictionaryService;

@RestController
@RequestMapping("api/v1/dictionaries")
@RequiredArgsConstructor
@Slf4j
public class DictionaryController {

    private final DictionaryService dictionaryService;

    @PostMapping
    public ResponseEntity<DictionaryDto> saveDictionary(@RequestBody CreateDictionaryCommand command){
        log.info("saveDictionary({})", command);
        return ResponseEntity.status(HttpStatus.CREATED).body(dictionaryService.save(command));
    }
    // todo
    // dodawanie wartosci do slownika

    // edycja wartosci w slowniku


    // pobranie slownika po id


    // usuniecia chce zeby byly zrobione za pomoca soft delete, czyli po protu ustawiamy jakis status np dodajemy pole w dictionary deleted ktore ma wartosc false, a jak usuniemy to ma sie zmienic na true
    // sprawdzic adnotacje @Where i @SoftDelete

    // usuniecie slownika

    // usuniecie wartosci ze slownika


}
