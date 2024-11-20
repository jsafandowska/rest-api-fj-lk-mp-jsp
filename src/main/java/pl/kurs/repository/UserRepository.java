package pl.kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kurs.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

}
