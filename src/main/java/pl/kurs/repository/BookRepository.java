package pl.kurs.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.model.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
