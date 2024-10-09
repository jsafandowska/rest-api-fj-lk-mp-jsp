package pl.kurs.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.dictionary.model.DictionaryValue;

import java.util.Optional;

public interface DictionaryValueRepository extends JpaRepository<DictionaryValue, Integer> {
    Optional<DictionaryValue> findByDictionaryNameAndValue(String name, String value);
    Optional<DictionaryValue> findByIdAndDictionaryId(int valueId, int dictionaryId);
}
