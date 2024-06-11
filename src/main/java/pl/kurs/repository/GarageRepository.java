package pl.kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.model.Garage;

import java.util.List;

public interface GarageRepository extends JpaRepository<Garage,Integer> {
    @Query("SELECT g FROM Garage g JOIN FETCH g.cars")
    List<Garage> findAllWithCars();
}
