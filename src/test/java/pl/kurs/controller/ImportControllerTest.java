package pl.kurs.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.model.ImportStatus;
import pl.kurs.service.ImportService;
import java.io.ByteArrayInputStream;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("tests")
public class ImportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ImportService importService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldImportBooks() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("books", "books.csv", "text/csv", "sample data".getBytes());
        ImportStatus importStatus = new ImportStatus();
        importStatus.setId(1);
        when(importService.startImport(anyString())).thenReturn(importStatus);
        String resposnseString = mockMvc.perform(multipart("/api/v1/import/books").file(mockFile))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1))
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(importService, times(1)).startImport(anyString());
        verify(importService, times(1)).importBooks(any(ByteArrayInputStream.class), eq(1));
    }

    @Test
    public void shouldGetImportStatus() throws Exception {
        ImportStatus importStatus = new ImportStatus();
        importStatus.setId(1);
        importStatus.setStatus(ImportStatus.Status.SUCCESS);
        when(importService.findById(1)).thenReturn(importStatus);
        mockMvc.perform(get("/api/v1/import/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
        verify(importService, times(1)).findById(1);
        Assertions.assertEquals(ImportStatus.Status.SUCCESS, importStatus.getStatus());
    }
    /*

    @Test
    public void shouldFindImportById() throws Exception {
        LocalDateTime start = LocalDateTime.of(2020, 10, 20, 20, 20, 20);
        ImportStatus importStatus = new ImportStatus("test", start);
        importStatusRepository.saveAndFlush(importStatus);
        Mockito.when(importDateProvider.provideNow()).thenReturn(start);
        MvcResult result = postman.perform(get("/api/imports/{id}", importStatus.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();


        String response = result.getResponse().getContentAsString();
        ImportStatusDto importDto = objectMapper.readValue(response, new TypeReference<>() {
        });
        verify(importStatusRepository, times(1)).findById(importStatus.getId());
        assertImportsEquals(importStatus, importDto);
    }

    @Test
    public void shouldThrowImportNotFoundExceptionWhenIdNotFound() throws Exception {
        long nonExistentEventId = 0;
        postman.perform(get("/api/imports/{id}", nonExistentEventId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(importStatusRepository, times(1)).findById(nonExistentEventId);
    }

    private void assertImportsEquals(ImportStatus importStatus, ImportStatusDto importStatusDto) {
        assertEquals(importStatus.getId(), importStatusDto.id());
        assertEquals(importStatus.getStatus(), importStatusDto.status());
        assertEquals(importStatus.getFailReason(), importStatusDto.failReason());
        assertEquals(importStatus.getFileName(), importStatusDto.fileName());
        assertEquals(importStatus.getProcessedRows(), importStatusDto.processedRows());
        assertEquals(importStatus.getStartDate(), importStatusDto.startDate());
        assertEquals(importStatus.getFinishDate(), importStatusDto.finishDate());
        assertEquals(importStatus.getSubmitDate(), importStatusDto.submitDate());
    }

     */
}