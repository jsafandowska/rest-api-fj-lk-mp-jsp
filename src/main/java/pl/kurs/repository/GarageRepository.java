package pl.kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.model.Garage;

import java.util.List;

public interface GarageRepository extends JpaRepository<Garage,Integer> {
    @Query("select g from Garage g join fetch g.cars")
    List<Garage> findAllWithCars();
}
