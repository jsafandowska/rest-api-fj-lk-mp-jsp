package pl.kurs.model.dto;


import pl.kurs.model.Author;

public record AuthorDto(int id, String name, String surname, Integer birthYear, Integer deathYear) {

    public static AuthorDto toDto(Author author) {
        return new AuthorDto(author.getId(),
                author.getName(),
                author.getSurname(),
                author.getBirthYear(),
                author.getDeathYear());
    }
}
