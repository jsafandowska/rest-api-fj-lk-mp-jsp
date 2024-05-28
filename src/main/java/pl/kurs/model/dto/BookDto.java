package pl.kurs.model.dto;

import lombok.Value;
import pl.kurs.model.Book;

public record BookDto(int id, String title, String category, boolean available, Integer authorId) {

    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(),
                book.getTitle(),
                book.getCategory(),
                book.isAvailable(),
                book.getAuthor().getId());
    }
}
