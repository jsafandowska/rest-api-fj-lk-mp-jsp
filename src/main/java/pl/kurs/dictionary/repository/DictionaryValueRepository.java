package pl.kurs.dictionary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kurs.dictionary.model.DictionaryValue;

import java.util.Optional;

public interface DictionaryValueRepository extends JpaRepository<DictionaryValue, Integer> {
    Optional<DictionaryValue> findByDictionaryNameAndValue(String name, String value);

    Optional<DictionaryValue> findByIdAndDictionaryId(int valueId, int dictionaryId);

    @Modifying
    @Query("UPDATE DictionaryValue dv SET dv.deleted = true WHERE dv.id = :valueId AND dv.dictionary.id = :dictionaryId")
    void softDeleteSingleDictionaryValue(@Param("valueId") int valueId, @Param("dictionaryId") int dictionaryId);

    @Modifying
    @Query("UPDATE DictionaryValue dv SET dv.deleted = true WHERE dv.dictionary.id = :dictionaryId")
    void softDeleteDictionaryValues(@Param("dictionaryId") int dictionaryId);
}
