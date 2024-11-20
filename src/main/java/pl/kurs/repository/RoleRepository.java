package pl.kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.model.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
}
