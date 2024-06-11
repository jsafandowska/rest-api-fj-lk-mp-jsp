package pl.kurs.repository;

import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.model.Author;
import pl.kurs.model.Garage;

import java.util.List;


public interface GarageRepository extends JpaRepository<Garage,Integer> {
    @Query("select a from Garage a join fetch a.cars")
    List<Garage> findAllWithCars();
}
