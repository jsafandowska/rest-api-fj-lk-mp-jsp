package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.model.Author;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.dto.AuthorDto;
import pl.kurs.repository.AuthorRepository;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public Page<AuthorDto> getAllAuthorsWithBooks(Pageable pageable) {
        return authorRepository.findAllWithBooks(pageable);
    }


    public Author save(CreateAuthorCommand command) {
        return authorRepository.saveAndFlush(new Author(command.getName(), command.getSurname(), command.getBirthYear(), command.getDeathYear()));
    }
}
