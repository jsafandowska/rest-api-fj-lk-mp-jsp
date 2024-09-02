package pl.kurs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.Author;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;
import pl.kurs.model.dto.AuthorDto;
import pl.kurs.service.AuthorService;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<Page<AuthorDto>> getAllAuthors(@PageableDefault Pageable pageable){
        return ResponseEntity.ok(authorService.getAllAuthorsWithBooks(pageable).map(AuthorDto::toDto));
    }
    @PostMapping
    public ResponseEntity<AuthorDto> addAuthor(@RequestBody CreateAuthorCommand command){
        return ResponseEntity.status(HttpStatus.CREATED).body(AuthorDto.toDto(authorService.addAuthor(command)));
    }
    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> findAuthor(@PathVariable int id){
        return ResponseEntity.ok(AuthorDto.toDto(authorService.findAuthor(id)));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorDto> deleteAuthor(@PathVariable int id){
        authorService.deleteAuthor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> editAuthor(@PathVariable int id, @RequestBody CreateAuthorCommand command){
        return ResponseEntity.status(HttpStatus.OK).body(AuthorDto.toDto(authorService.editAuthor(id, command)));
    }
    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<AuthorDto> editAuthorPartially(@PathVariable int id, @RequestBody EditAuthorCommand command){
        return ResponseEntity.status(HttpStatus.OK).body(AuthorDto.toDto(authorService.editAuthorPartially(id, command)));
    }

}
