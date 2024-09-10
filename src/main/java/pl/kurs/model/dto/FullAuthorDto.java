package pl.kurs.model.dto;

import lombok.Getter;


public record FullAuthorDto(int id, String name, String surname, Integer birthYear, Integer deathYear,
                            long amountOfBooks) {
}
