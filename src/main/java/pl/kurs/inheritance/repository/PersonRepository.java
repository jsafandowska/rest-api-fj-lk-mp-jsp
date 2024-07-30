package pl.kurs.inheritance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.inheritance.model.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {
}
