package pl.kurs.service;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import pl.kurs.faade.ImportStatusFacade;
import pl.kurs.model.ImportStatus;
import java.io.*;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ActiveProfiles("tests")
public class ImportServiceTest {

    @Mock
    private ImportStatusFacade importStatusFacade;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private ImportService importService;
    private ImportStatus importStatus;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        importStatus = new ImportStatus("test.csv");
        importStatus.setId(1);
        importStatus.setProcessed(0);
    }

    @Test
    public void shouldStartImport() {
        when(importStatusFacade.saveAndFlush(any(ImportStatus.class))).thenReturn(importStatus);
        ImportStatus result = importService.startImport("test.csv");
        assertEquals("test.csv", result.getFileName());
        verify(importStatusFacade, times(1)).saveAndFlush(any(ImportStatus.class));
    }

    @Test
    public void shouldImportBooksSuccessfully() throws IOException {
        String csvData = "T1,C1,1\nT2,C2,2";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes());
        when(importStatusFacade.findById(1)).thenReturn(Optional.of(importStatus));
        doNothing().when(importStatusFacade).updateToProcessing(1);
        doNothing().when(importStatusFacade).updateToSuccess(1, 2);
        doNothing().when(importStatusFacade).updateProgress(anyInt(), anyInt());
        importService.importBooks(inputStream, 1);
        verify(importStatusFacade, times(1)).updateToProcessing(1);
        verify(importStatusFacade, times(1)).updateToSuccess(1, 2);
        verify(importStatusFacade, never()).updateToFail(anyInt(), anyString(), anyInt());
    }

    @Test
    public void shouldHandleImportBooksException() throws IOException {
        InputStream faultyInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Test Exception");
            }
        };
        when(importStatusFacade.findById(1)).thenReturn(Optional.of(importStatus));
        doNothing().when(importStatusFacade).updateToProcessing(1);
        doNothing().when(importStatusFacade).updateToFail(anyInt(), anyString(), anyInt());

        IllegalStateException thrownException = Assertions.assertThrows(IllegalStateException.class, () -> {
            importService.importBooks(faultyInputStream, 1);
        });

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(thrownException.getMessage()).isEqualTo("wycofanie transakcji");
        softAssertions.assertAll();
        verify(importStatusFacade, times(1)).updateToProcessing(1);
        verify(importStatusFacade, times(1)).updateToFail(eq(1), anyString(), eq(0));
        verify(importStatusFacade, never()).updateToSuccess(anyInt(), anyInt());
    }
    @Test
    public void shouldSaveBook() {
        String[] args = {"A", "B", "1"};
        when(jdbcTemplate.update("insert into book(title, category, available, author_id) values (?,?,?,?)",
                args[0], args[1], true, Integer.parseInt(args[2]))).thenReturn(1);
        importService.saveBook(args);
        verify(jdbcTemplate, times(1)).update(
                "insert into book(title, category, available, author_id) values (?,?,?,?)",
                args[0], args[1], true, Integer.parseInt(args[2])
        );
    }

    /*
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        importStatusService = new ImportStatusService(importStatusRepository, entityManager, updateService, importDateProvider, processConfig);
    }

    @Test
    public void shouldInitializeImport() {
        String fileName = "test.csv";
        ImportStatus expectedImportStatus = new ImportStatus(fileName, START);
        when(dateProvider.provideNow()).thenReturn(START);
        when(importStatusRepository.save(any(ImportStatus.class))).thenReturn(expectedImportStatus);

        ImportStatus actualImportStatus = importStatusService.initializeImport(fileName);

        verify(importStatusRepository, times(1)).save(any(ImportStatus.class));
        assertNotNull(actualImportStatus);
        assertEquals(expectedImportStatus.getId(), actualImportStatus.getId());
        assertEquals(expectedImportStatus.getStatus(), actualImportStatus.getStatus());
        assertEquals(expectedImportStatus.getFileName(), actualImportStatus.getFileName());
        assertEquals(expectedImportStatus.getStartDate(), actualImportStatus.getStartDate());
    }

    @Test
    public void shouldGetImportStatus() {
        ImportStatus expectedImportStatus = new ImportStatus("test", START);
        long id = expectedImportStatus.getId();
        when(importStatusRepository.findById(id)).thenReturn(Optional.of(expectedImportStatus));

        ImportStatus returnedImportedStatus = importStatusService.getImportStatus(expectedImportStatus.getId());


        verify(importStatusRepository, times(1)).findById(id);

        assertNotNull(returnedImportedStatus);
        assertEquals(expectedImportStatus.getId(), returnedImportedStatus.getId());
        assertEquals(expectedImportStatus.getFileName(), returnedImportedStatus.getFileName());
    }

    @Test
    public void whenImportStatusDoesNotExistThenShouldThrowException() {
        long id = 1L;
        when(importStatusRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ImportNotFoundException.class, () -> importStatusService.getImportStatus(id));
    }

    @Test
    public void shouldCorrectlyIncrementCounterAndCallUpdateServiceAndDone() {
        String input = "line1\nline2\nline3\nline4";
        int numbersOfRows = input.split("\n").length;
        InputStream testInputStream = new ByteArrayInputStream(input.getBytes());
        Importable mockImportable = mock(Importable.class);
        long importId = 123L;
        when(processConfig.getBatchSize()).thenReturn(2L);

        importStatusService.importObj(testInputStream, importId, mockImportable);

        ArgumentCaptor<Long> importIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> counterCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(updateService, times(2)).updateProgress(importIdCaptor.capture(), counterCaptor.capture());
        verify(entityManager, times(2)).clear();
        assertEquals(Long.valueOf(123L), importIdCaptor.getValue());
        assertEquals(Integer.valueOf(numbersOfRows), counterCaptor.getValue());

        verify(mockImportable, times(numbersOfRows)).saveForImport(anyString());
        verify(updateService, times(1)).done(importIdCaptor.capture(), counterCaptor.capture());
        assertEquals(Long.valueOf(123L), importIdCaptor.getValue());
        assertEquals(Integer.valueOf(numbersOfRows), counterCaptor.getValue());
    }

    @Test
    public void shouldCallUpdateServiceErrorOnException() {
        String line = "line\n";
        InputStream testInputStream = new ByteArrayInputStream(line.getBytes());
        int numbersOfRows = line.split("\n").length;
        Importable mockImportable = mock(Importable.class);
        long importId = 123L;


        when(processConfig.getBatchSize()).thenReturn(2L);
        doThrow(new RuntimeException("Test exception")).when(mockImportable).saveForImport(anyString());

        ArgumentCaptor<Integer> counterCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        assertThrows(RuntimeException.class, () -> importStatusService.importObj(testInputStream, importId, mockImportable));

        verify(updateService, times(1)).error(eq((importId)), counterCaptor.capture(), messageCaptor.capture());
        assertEquals(counterCaptor.getValue(), numbersOfRows);
        assertThat(messageCaptor.getValue()).isEqualTo("Test exception");
    }

     */
}

