package pl.kurs.model.dto;

import pl.kurs.model.User;

import java.util.Set;
import java.util.stream.Collectors;

public record UserDto(
        Long id,
        String name,
        String surname,
        String email
) {
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail()
        );
    }
}
