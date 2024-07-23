package pl.kurs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.model.ImportStatus;

public interface ImportStatusRepository extends JpaRepository<ImportStatus, Integer> {
}