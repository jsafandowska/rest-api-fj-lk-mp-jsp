package pl.kurs.repository;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.model.Author;
import pl.kurs.model.dto.AuthorDto;
import pl.kurs.model.dto.FullAuthorDto;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    @Query("select a from Author a left join fetch a.books")
    List<Author> findAllWithBooks();

    @Query(value = "select new pl.kurs.model.dto.FullAuthorDto(a.id, a.name, a.surname, a.birthYear, a.deathYear, " +
            "(select count(b) from Book b where b.author.id = a.id) as amountOfBooks) from Author a",
    countQuery = "select count(a) from Author a")
    Page<FullAuthorDto> findAllWithBooks(Pageable pageable);


    @Query("select a from Author a left join fetch a.books where a.id = ?1")
    Optional<Author> findByIdWithBooks(int id);
}
