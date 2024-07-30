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
    public void testImportBooks() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("books", "books.csv", "text/csv", "sample data".getBytes());
        ImportStatus importStatus = new ImportStatus();
        importStatus.setId(1);
        when(importService.startImport(anyString())).thenReturn(importStatus);
        mockMvc.perform(multipart("/api/v1/import/books").file(mockFile))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1));
        verify(importService, times(1)).startImport(anyString());
        verify(importService, times(1)).importBooks(any(ByteArrayInputStream.class), eq(1));
    }

    @Test
    public void testGetImportStatus() throws Exception {
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
}