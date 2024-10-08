package pl.kurs.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kurs.dictionary.model.Dictionary;

import java.util.Optional;

public interface DictionaryRepository extends JpaRepository<Dictionary, Integer> {
    @Query("SELECT d FROM Dictionary d LEFT JOIN FETCH d.values WHERE d.id = ?1")
    Optional<Dictionary> findByIdWithValues(@Param("id") int id);
}
