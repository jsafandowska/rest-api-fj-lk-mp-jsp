package pl.kurs.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.dictionary.model.command.CreateDictionaryCommand;
import pl.kurs.dictionary.model.command.CreateDictionaryValueCommand;
import pl.kurs.dictionary.model.command.EditDictionaryValueCommand;
import pl.kurs.dictionary.model.dto.DictionaryDto;
import pl.kurs.dictionary.model.dto.DictionaryValueDto;
import pl.kurs.dictionary.repository.DictionaryRepository;
import pl.kurs.dictionary.repository.DictionaryValueRepository;
import pl.kurs.exceptions.DictionaryNotFoundException;

@Service
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private final DictionaryValueRepository dictionaryValueRepository;

    public DictionaryDto save(CreateDictionaryCommand command) {
        return DictionaryDto.toDto(dictionaryRepository.saveAndFlush(new Dictionary(command.getName(), command.getLaunchValues())));
    }

// dodawanie wartosci do slownika

    public DictionaryValueDto addValueToDictionary(int dictionaryId, CreateDictionaryValueCommand command) {
        Dictionary dictionary = dictionaryRepository.findByIdWithValues(dictionaryId)
                .orElseThrow(() -> new DictionaryNotFoundException("Dictionary not found"));
        DictionaryValue dictionaryValue = new DictionaryValue(command.getValue(), dictionary);
        dictionaryValueRepository.save(dictionaryValue);
        return DictionaryValueDto.toDto(dictionaryValue);
    }

    // edycja wartosci w slowniku
    @Transactional
    @CachePut(cacheNames = "dictionaries", key = "#id")
    public DictionaryValueDto updateDictionaryValue(int dictionaryId, int valueId, EditDictionaryValueCommand command) {
        DictionaryValue dictionaryValue = dictionaryValueRepository.findByIdAndDictionaryId(valueId, dictionaryId)
                .orElseThrow(() -> new DictionaryNotFoundException("Dictionary value not found"));
        dictionaryValue.setValue(command.getValue());
        dictionaryValueRepository.save(dictionaryValue);
        return DictionaryValueDto.toDto(dictionaryValue);
    }

    // pobranie slownika po id
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "dictionaries", key = "#id")
    public DictionaryDto findById(int id) {
        System.out.println("odpalam metode w serwisie");
        Dictionary dictionary = dictionaryRepository.findByIdWithValues(id)
                .orElseThrow(() -> new DictionaryNotFoundException("Dictionary not found"));
        return DictionaryDto.toDto(dictionary);
    }


// usuniecia chce zeby byly zrobione za pomoca soft delete, czyli po protu ustawiamy jakis status np dodajemy pole w dictionary deleted ktore ma wartosc false, a jak usuniemy to ma sie zmienic na true
// sprawdzic adnotacje @Where i @SoftDelete

    // usuniecie slownika
    @Transactional
    @CacheEvict(cacheNames = "dictionaries", key = "#id")
    public void deleteDictionary(int id) {
        dictionaryRepository.deleteById(id);
        deleteAllValuesFromDictionary(id);
        dictionaryRepository.softDeleteDictionary(id);
    }

    @Transactional
    public void deleteDictionaryValue(int dictionaryId, int valueId) {
        dictionaryValueRepository.softDeleteSingleDictionaryValue(valueId, dictionaryId);
    }

    @Transactional
    public void deleteAllValuesFromDictionary(int dictionaryId) {
        dictionaryValueRepository.softDeleteDictionaryValues(dictionaryId);
    }
}






