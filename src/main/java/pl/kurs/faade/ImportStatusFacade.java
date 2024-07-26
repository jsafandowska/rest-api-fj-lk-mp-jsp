package pl.kurs.faade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.model.ImportStatus;
import pl.kurs.repository.ImportStatusRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImportStatusFacade {

    private final ImportStatusRepository importStatusRepository;


    public Optional<ImportStatus> findById(int id) {
        return importStatusRepository.findById(id);
    }

    public ImportStatus saveAndFlush(ImportStatus importStatus) {
        return importStatusRepository.saveAndFlush(importStatus);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateToProcessing(int id) {
        ImportStatus importStatus = importStatusRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid import ID"));
        importStatus.setStatus(ImportStatus.Status.PROCESSING);
        importStatus.setStartDate(LocalDateTime.now());
        importStatusRepository.saveAndFlush(importStatus);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateProgress(int id, int progress) {
        ImportStatus importStatus = importStatusRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid import ID"));
        importStatus.setProcessed(progress);
        importStatusRepository.saveAndFlush(importStatus);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateToSuccess(int id, int processed) {
        ImportStatus importStatus = importStatusRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid import ID"));
        importStatus.setStatus(ImportStatus.Status.SUCCESS);
        importStatus.setFinishDate(LocalDateTime.now());
        importStatus.setProcessed(processed);
        importStatusRepository.saveAndFlush(importStatus);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateToFail(int id, String message, int processed) {
        ImportStatus importStatus = importStatusRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid import ID"));
        importStatus.setFinishDate(LocalDateTime.now());
        importStatus.setStatus(ImportStatus.Status.FAILED);
        importStatus.setFailedReason(message);
        importStatus.setProcessed(processed);
        importStatusRepository.saveAndFlush(importStatus);
    }

}
