package pl.kurs.pesimistic.lock.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.pesimistic.lock.example.model.Visit;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
}
