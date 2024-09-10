package pl.kurs.pesimistic.lock.example.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.pesimistic.lock.example.model.Doctor;

import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    @Query("select d from Doctor d where d.id = ?1")
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Doctor> findByIdWithLocking(Integer doctorId);
}
