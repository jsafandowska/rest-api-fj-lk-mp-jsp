package pl.kurs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{id}")
    public ResponseEntity<ImportStatus> getImportStatus(@PathVariable int id) {
        ImportStatus actualImport = importService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(actualImport);
    }
}