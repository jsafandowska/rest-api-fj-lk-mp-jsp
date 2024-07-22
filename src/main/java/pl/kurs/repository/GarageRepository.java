package pl.kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.model.Garage;

public interface GarageRepository extends JpaRepository<Garage,Integer> {
}
