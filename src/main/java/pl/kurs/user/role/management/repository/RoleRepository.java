package pl.kurs.user.role.management.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.user.role.management.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
