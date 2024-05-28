package pl.kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.model.Car;

public interface CarRepository extends JpaRepository<Car,Integer> {
}
