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
    public void shouldHandleImportBooksException() {
        InputStream faultyInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("Test Exception");
            }
        };

        when(importStatusFacade.findById(1)).thenReturn(Optional.of(importStatus));
        doNothing().when(importStatusFacade).updateToProcessing(1);

        UncheckedIOException exception = Assertions.assertThrows(UncheckedIOException.class, () -> {
            importService.importBooks(faultyInputStream, 1);
        });

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(exception.getCause()).isInstanceOf(IOException.class);
        softAssertions.assertThat(exception.getCause().getMessage()).isEqualTo("Test Exception");
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
}

