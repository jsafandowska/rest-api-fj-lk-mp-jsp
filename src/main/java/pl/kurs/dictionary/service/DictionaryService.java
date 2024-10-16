package pl.kurs.dictionary.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
                .orElseThrow(() -> new EntityNotFoundException("Dictionary not found"));
        DictionaryValue dictionaryValue = new DictionaryValue(command.getValue(), dictionary);
        dictionaryValueRepository.save(dictionaryValue);
        return DictionaryValueDto.toDto(dictionaryValue);
    }

    // edycja wartosci w slowniku
    public DictionaryValueDto updateDictionaryValue(int dictionaryId, int valueId, EditDictionaryValueCommand command) {
        DictionaryValue dictionaryValue = dictionaryValueRepository.findByIdAndDictionaryId(valueId, dictionaryId)
                .orElseThrow(() -> new EntityNotFoundException("Dictionary value not found"));
        dictionaryValue.setValue(command.getValue());
        dictionaryValueRepository.save(dictionaryValue);
        return DictionaryValueDto.toDto(dictionaryValue);
    }

    // pobranie slownika po id
    public DictionaryDto findById(int id) {
        Dictionary dictionary = dictionaryRepository.findByIdWithValues(id)
                .orElseThrow(() -> new EntityNotFoundException("Dictionary not found"));
        return DictionaryDto.toDto(dictionary);
    }


// usuniecia chce zeby byly zrobione za pomoca soft delete, czyli po protu ustawiamy jakis status np dodajemy pole w dictionary deleted ktore ma wartosc false, a jak usuniemy to ma sie zmienic na true
// sprawdzic adnotacje @Where i @SoftDelete

    // usuniecie slownika
    @Transactional
    public void deleteDictionary(int id) {
//        dictionaryRepository.deleteById(id);
        deleteAllValuesFromDictionary(id);
        dictionaryRepository.softDeleteDictionary(id);
    }

    // usuniecie wartosci ze slownika
    @Transactional
    public void deleteDictionaryValue(int dictionaryId, int valueId) {
        dictionaryValueRepository.softDeleteSingleDictionaryValue(valueId, dictionaryId);
    }

    @Transactional
    public void deleteAllValuesFromDictionary(int dictionaryId) {
        dictionaryValueRepository.softDeleteDictionaryValues(dictionaryId);
    }
}






