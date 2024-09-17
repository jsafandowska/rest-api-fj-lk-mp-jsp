package pl.kurs.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.dictionary.model.Dictionary;

public interface DictionaryRepository extends JpaRepository<Dictionary, Integer> {
}
