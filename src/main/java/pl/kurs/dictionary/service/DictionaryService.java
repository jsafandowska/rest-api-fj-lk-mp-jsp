package pl.kurs.dictionary.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.command.CreateDictionaryCommand;
import pl.kurs.dictionary.model.dto.DictionaryDto;
import pl.kurs.dictionary.repository.DictionaryRepository;

@Service
@RequiredArgsConstructor
public class DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    public DictionaryDto save(CreateDictionaryCommand command) {
        return DictionaryDto.toDto(dictionaryRepository.saveAndFlush(new Dictionary(command.getName(), command.getLaunchValues())));
    }
}
