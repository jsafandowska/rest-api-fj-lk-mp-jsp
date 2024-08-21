package pl.kurs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.kurs.model.Author;
import pl.kurs.model.dto.AuthorDto;
import pl.kurs.service.AuthorService;

@RestController
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public Page<AuthorDto> getAllAuthors(@PageableDefault Pageable pageable){
        return authorService.getAllAuthorsWithBooks(pageable);
    }

}
