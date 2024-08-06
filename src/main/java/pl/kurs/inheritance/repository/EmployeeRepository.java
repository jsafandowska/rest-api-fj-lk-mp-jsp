package pl.kurs.inheritance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.inheritance.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
}
