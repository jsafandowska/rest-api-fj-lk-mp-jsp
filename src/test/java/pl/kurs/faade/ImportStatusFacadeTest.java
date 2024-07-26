package pl.kurs.faade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.kurs.model.ImportStatus;
import pl.kurs.repository.ImportStatusRepository;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ImportStatusFacadeTest {
    @Mock
    private ImportStatusRepository importStatusRepository;
    @InjectMocks
    private ImportStatusFacade importStatusFacade;
    private ImportStatus importStatus;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        importStatus = new ImportStatus();
        importStatus.setId(1);
        importStatus.setStatus(ImportStatus.Status.NEW);
        importStatus.setProcessed(0);
    }

    @Test
    public void shouldFindById() {
        when(importStatusRepository.findById(1)).thenReturn(Optional.of(importStatus));
        Optional<ImportStatus> result = importStatusFacade.findById(1);
        assertEquals(importStatus.getStatus(), result.get().getStatus());
        verify(importStatusRepository,times(1)).findById(1);
    }

    @Test
    public void shouldSaveAndFlush() {
        when(importStatusRepository.saveAndFlush(importStatus)).thenReturn(importStatus);
        ImportStatus result = importStatusFacade.saveAndFlush(importStatus);
        assertEquals(importStatus, result);
        verify(importStatusRepository,times(1)).saveAndFlush(result);
    }

    @Test
    public void shouldUpdateToProcessing() {
        when(importStatusRepository.findById(1)).thenReturn(Optional.of(importStatus));
        importStatusFacade.updateToProcessing(1);
        assertEquals(ImportStatus.Status.PROCESSING, importStatus.getStatus());
        verify(importStatusRepository,times(1)).saveAndFlush(importStatus);
    }

    @Test
    public void shouldUpdateProgress() {
        when(importStatusRepository.findById(1)).thenReturn(Optional.of(importStatus));
        importStatusFacade.updateProgress(1, 50);
        assertEquals(50, importStatus.getProcessed());
        verify(importStatusRepository,times(1)).saveAndFlush(importStatus);
    }

    @Test
    public void shouldUpdateToSuccess() {
        when(importStatusRepository.findById(1)).thenReturn(Optional.of(importStatus));
        importStatusFacade.updateToSuccess(1, 100);
        assertEquals(ImportStatus.Status.SUCCESS, importStatus.getStatus());
        assertEquals(100, importStatus.getProcessed());
        verify(importStatusRepository,times(1)).saveAndFlush(importStatus);
    }

    @Test
    public void shouldUpdateToFail() {
        when(importStatusRepository.findById(1)).thenReturn(Optional.of(importStatus));
        importStatusFacade.updateToFail(1, "error message", 75);
        assertEquals(ImportStatus.Status.FAILED, importStatus.getStatus());
        assertEquals("error message", importStatus.getFailedReason());
        assertEquals(75, importStatus.getProcessed());
        verify(importStatusRepository,times(1)).saveAndFlush(importStatus);
    }
}