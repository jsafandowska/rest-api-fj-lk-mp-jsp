package pl.kurs.inheritance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.inheritance.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
