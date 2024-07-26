package pl.kurs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ImportStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime submitDate;
    private LocalDateTime startDate;
    private Status status;
    private LocalDateTime finishDate;
    private String failedReason;
    private int processed;
    private String fileName;

    public ImportStatus(String fileName) {
        this.submitDate = LocalDateTime.now();
        this.status = Status.NEW;
        this.fileName = fileName;
    }

    public enum Status{
        NEW, PROCESSING, SUCCESS, FAILED
    }
}
