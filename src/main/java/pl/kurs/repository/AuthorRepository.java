package pl.kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.model.Author;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    @Query("select a from Author a join fetch a.books")
    List<Author> findAllWithBooks();
}
