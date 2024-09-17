package pl.kurs.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.dictionary.model.DictionaryValue;

public interface DictionaryValueRepository extends JpaRepository<DictionaryValue, Integer> {
}
