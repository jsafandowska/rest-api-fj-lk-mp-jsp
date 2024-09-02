package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.exceptions.AuthorNotFoundException;
import pl.kurs.model.Author;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.model.command.EditAuthorCommand;
import pl.kurs.model.dto.AuthorDto;
import pl.kurs.repository.AuthorRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional(readOnly = true)
    public Page<Author> getAllAuthorsWithBooks(Pageable pageable) {
        return authorRepository.findAllWithBooks(pageable);
    }
    @Transactional
    public Author addAuthor(CreateAuthorCommand command) {
        return authorRepository.saveAndFlush(new Author(command.getName(), command.getSurname(), command.getBirthYear(), command.getDeathYear()));
    }
    @Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
    public Author findAuthor(int id) {
        return authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    public void deleteAuthor(int id){
        authorRepository.deleteById(id);
    }
    public Author editAuthor(int id, CreateAuthorCommand command){
        Author author = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        author.setName(command.getName());
        author.setSurname(command.getSurname());
        author.setBirthYear(command.getBirthYear());
        author.setDeathYear(command.getDeathYear());
        return authorRepository.saveAndFlush(author);
    }

    public Author editAuthorPartially(int id, EditAuthorCommand command){
        Author author = authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
        Optional.ofNullable(command.getName()).ifPresent(author::setName);
        Optional.ofNullable(command.getSurname()).ifPresent(author::setSurname);
        Optional.ofNullable(command.getBirthYear()).ifPresent(author::setBirthYear);
        Optional.ofNullable(command.getDeathYear()).ifPresent(author::setDeathYear);
        return authorRepository.saveAndFlush(author);
    }



}
