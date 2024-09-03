package pl.kurs.model.dto;


public record FullAuthorDto(int id, String name, String surname, Integer birthYear, Integer deathYear,
                            long amountOfBooks) {
}
