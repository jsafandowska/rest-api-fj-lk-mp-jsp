package pl.kurs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;
import pl.kurs.model.dto.AuthorDto;
import pl.kurs.model.dto.FullAuthorDto;
import pl.kurs.service.AuthorService;


@RestController
@RequestMapping("/api/v1/authors")
@Slf4j
@RequiredArgsConstructor

public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
        public ResponseEntity<Page<FullAuthorDto>> getAllAuthors(@PageableDefault Pageable pageable){
        log.info("findAll");
        return ResponseEntity.ok(authorService.getAllAuthorsWithBooks(pageable));
    }
    @PostMapping
    public ResponseEntity<AuthorDto> addAuthor(@RequestBody @Valid CreateAuthorCommand command){
        log.info("addAuthor({})", command);
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthorDto.toDto(authorService.addAuthor(command)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> findAuthor(@PathVariable int id){
        log.info("findAuthor({})", id);
        return ResponseEntity.ok(AuthorDto.toDto(authorService.findAuthor(id)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable int id){
        log.info("deleteAuthor({})", id);
        authorService.deleteAuthor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> editAuthor(@PathVariable int id, @RequestBody EditAuthorCommand command){
        log.info("editAuthor({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(AuthorDto.toDto(authorService.editAuthor(id, command)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorDto> editAuthorPartially(@PathVariable int id, @RequestBody EditAuthorCommand command){
        log.info("editAuthorPartially({}, {})", id, command);
        return ResponseEntity.status(HttpStatus.OK).body(AuthorDto.toDto(authorService.editAuthorPartially(id, command)));
    }
}
