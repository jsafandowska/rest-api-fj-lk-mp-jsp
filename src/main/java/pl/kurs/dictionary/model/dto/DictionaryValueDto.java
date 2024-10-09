package pl.kurs.dictionary.model.dto;

import pl.kurs.dictionary.model.DictionaryValue;

public record DictionaryValueDto(int id, String value, String dictionaryName, boolean deleted) {

    public static DictionaryValueDto toDto(DictionaryValue dictionaryValue) {
        return new DictionaryValueDto(
                dictionaryValue.getId(),
                dictionaryValue.getValue(),
                dictionaryValue.getDictionary().getName(),
                dictionaryValue.isDeleted()
        );
    }
}