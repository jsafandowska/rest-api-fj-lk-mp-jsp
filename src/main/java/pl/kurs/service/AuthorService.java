package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.exceptions.BookNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.model.dto.AuthorDto;
import pl.kurs.model.dto.FullAuthorDto;
import pl.kurs.repository.AuthorRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public Page<FullAuthorDto> getAllAuthorsWithBooks(Pageable pageable) {
        return authorRepository.findAllWithBooks(pageable);
    }

    public Author addAuthor(CreateAuthorCommand command) {
        return authorRepository.saveAndFlush(new Author(command.getName(), command.getSurname(), command.getBirthYear(), command.getDeathYear()));
    }


    @Transactional(readOnly = true)
    public Author findAuthor(int id) {
        return authorRepository.findById(id)
                .orElseThrow(AuthorNotFoundException::new);
    }

    public void deleteAuthor(int id) {
        authorRepository.deleteById(id);
    }

    @Transactional
    public Author editAuthor(int id, EditAuthorCommand command) {
        Author author = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        Author copy = new Author(author);
        copy.setName(command.getName());
        copy.setSurname(command.getSurname());
        copy.setDeathYear(command.getDeathYear());
        copy.setVersion(command.getVersion());
        return authorRepository.saveAndFlush(copy);
    }


    @Transactional
    public Author editAuthorPartially(int id, EditAuthorCommand command) {
        Author author = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        Optional.ofNullable(command.getName()).ifPresent(author::setName);
        Optional.ofNullable(command.getSurname()).ifPresent(author::setSurname);
        Optional.ofNullable(command.getDeathYear()).ifPresent(author::setDeathYear);
        return authorRepository.saveAndFlush(author);
    }


    public Author save(CreateAuthorCommand command) {
        return authorRepository.saveAndFlush(new Author(command.getName(), command.getSurname(), command.getBirthYear(), command.getDeathYear()));
    }
}
