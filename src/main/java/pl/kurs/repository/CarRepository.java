package pl.kurs.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.model.Car;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Integer> {
//    @Query("select c from Car c join fetch c.garage")
//    List<Car> findAllWithGarage();
}
