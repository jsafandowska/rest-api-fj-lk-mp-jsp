package pl.kurs.dictionary.model.dto;

import pl.kurs.dictionary.model.Dictionary;
import pl.kurs.dictionary.model.DictionaryValue;

import java.util.Set;
import java.util.stream.Collectors;

public record DictionaryDto(int id, String name, Set<String> values) {
    public static DictionaryDto toDto(Dictionary dictionary){
        return new DictionaryDto(dictionary.getId(),
                dictionary.getName(),
                dictionary.getValues().stream().map(DictionaryValue::getValue).collect(Collectors.toSet()));
    }
}
