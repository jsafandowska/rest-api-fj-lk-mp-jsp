package pl.kurs.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.model.Book;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {

}


