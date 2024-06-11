package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;
import pl.kurs.model.dto.AuthorDto;
import pl.kurs.repository.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorDto> findAll() {
        return authorRepository.findAll().stream().map(AuthorDto::toDto).toList();
    }

    public AuthorDto addAuthor(CreateAuthorCommand command) {
        Author author = new Author(command.getName(), command.getSurname(), command.getBirthYear(), command.getDeathYear());
        author = authorRepository.saveAndFlush(author);
        return AuthorDto.toDto(author);
    }

    public AuthorDto findAuthor(int id) {
        Author author = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        return AuthorDto.toDto(author);
    }

    public void deleteAuthor(int id) {
        authorRepository.deleteById(id);
    }

    public AuthorDto editAuthor(int id, EditAuthorCommand command) {
        Author author = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        author.setName(command.getName());
        author.setSurname(command.getSurname());
        author.setBirthYear(command.getBirthYear());
        author.setDeathYear(command.getDeathYear());
        author = authorRepository.saveAndFlush(author);
        return AuthorDto.toDto(author);
    }

    public AuthorDto editAuthorPartially(int id, EditAuthorCommand command) {
        Author author = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        Optional.ofNullable(command.getName()).ifPresent(author::setName);
        Optional.ofNullable(command.getSurname()).ifPresent(author::setSurname);
        Optional.ofNullable(command.getBirthYear()).ifPresent(author::setBirthYear);
        Optional.ofNullable(command.getDeathYear()).ifPresent(author::setDeathYear);
        author = authorRepository.saveAndFlush(author);
        return AuthorDto.toDto(author);
    }
}
