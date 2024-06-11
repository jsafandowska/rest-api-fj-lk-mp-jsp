package pl.kurs.model.dto;

import pl.kurs.model.Author;

import java.util.List;

public record AuthorDto(int id, String name, String surname, Integer birthYear, Integer deathYear, List<BookDto> books) {

    public static AuthorDto toDto(Author author) {
        List<BookDto> bookDtos = author.getBooks().stream()
                                       .map(BookDto::toDto)
                                       .toList();
        return new AuthorDto(
                author.getId(),
                author.getName(),
                author.getSurname(),
                author.getBirthYear(),
                author.getDeathYear(),
                bookDtos
        );
    }
}
