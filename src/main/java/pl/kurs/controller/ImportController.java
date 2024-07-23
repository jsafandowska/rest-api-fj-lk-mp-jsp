package pl.kurs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.kurs.model.ImportStatus;
import pl.kurs.service.ImportService;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/import")
@RequiredArgsConstructor
public class ImportController {

    private final ImportService importService;

    @PostMapping("/books")
    public ResponseEntity<ImportStatus> importBooks(@RequestPart("books") MultipartFile file) throws IOException {
        ImportStatus actualImport = importService.startImport(file.getName());
        importService.importBooks(file.getInputStream(), actualImport.getId());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(actualImport);
    }
}