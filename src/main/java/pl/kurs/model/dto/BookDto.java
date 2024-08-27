package pl.kurs.model.dto;

import pl.kurs.model.Book;

public record BookDto(int id, String title, String category, boolean available, Integer authorId, Long version) {

    public static BookDto toDto(Book book) {
        return new BookDto(book.getId(),
                book.getTitle(),
                book.getCategory(),
                book.isAvailable(),
                book.getAuthor().getId(),
                book.getVersion());
    }
}
