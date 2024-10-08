package pl.kurs.dictionary.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.dictionary.model.command.CreateDictionaryCommand;
import pl.kurs.dictionary.model.command.CreateDictionaryValueCommand;
import pl.kurs.dictionary.model.command.EditDictionaryValueCommand;
import pl.kurs.dictionary.model.dto.DictionaryDto;
import pl.kurs.dictionary.model.dto.DictionaryValueDto;
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

    @PostMapping("/{dictionaryId}/values")
    public ResponseEntity<DictionaryValueDto> addValueToDictionary(@PathVariable int dictionaryId, @RequestBody CreateDictionaryValueCommand command) {
        log.info("addValueToDictionary(dictionaryId: {}, command: {})", dictionaryId, command);
        return ResponseEntity.status(HttpStatus.CREATED).body(dictionaryService.addValueToDictionary(dictionaryId, command));
    }

    @PutMapping("/{dictionaryId}/values/{valueId}")
    public ResponseEntity<DictionaryValueDto> updateDictionaryValue(@PathVariable int dictionaryId, @PathVariable int valueId, @RequestBody EditDictionaryValueCommand command) {
        log.info("updateDictionaryValue(dictionaryId: {}, valueId: {}, command: {})", dictionaryId, valueId, command);
        return ResponseEntity.status(HttpStatus.OK).body(dictionaryService.updateDictionaryValue(dictionaryId, valueId, command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DictionaryDto> findDictionaryById(@PathVariable int id) {
        log.info("getDictionaryById({})", id);
        return ResponseEntity.status(HttpStatus.OK).body(dictionaryService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDeleteDictionary(@PathVariable int id) {
        log.info("softDeleteDictionary({})", id);
        dictionaryService.softDeleteDictionary(id);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{dictionaryId}/values/{valueId}")
    public ResponseEntity<Void> softDeleteDictionaryValue(@PathVariable int dictionaryId, @PathVariable int valueId) {
        log.info("softDeleteDictionaryValue(dictionaryId: {}, valueId: {})", dictionaryId, valueId);
        dictionaryService.softDeleteDictionaryValue(dictionaryId, valueId);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
