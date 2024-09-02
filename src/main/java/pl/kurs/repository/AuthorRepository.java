package pl.kurs.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    //    @Query("select a from Author a left join fetch a.books")
//    Page<Author> findAllWithBooks(Pageable pageable);
    @Query(value = "SELECT a FROM Author a LEFT JOIN FETCH a.books",
            countQuery = "SELECT COUNT(a) FROM Author a")
    Page<Author> findAllWithBooks(Pageable pageable);
//    @Query(value = "select new pl.kurs.model.dto.AuthorDto(a.id, a.name, a.surname, a.birthYear, a.deathYear, (select count(b) from Book b where b.author.id = a.id)) from Author a",
//    countQuery = "select count(a) from Author a")
//    Page<AuthorDto> findAllWithBooks(Pageable pageable);

    /*

    /*
    A1:5
    A2:2
    A3:3
    select * from author ... join a.books
    A1B1
    A1B2
    A1B3
     */

    // migracja z numeru pesel na date urodzenia i plec
    // wykorzystac custom change task
}
