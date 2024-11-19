package pl.kurs.user.role.management.dto;

import pl.kurs.user.role.management.model.User;

public record UserDto(long id, String name, String surname, String email, String password) {
    public static UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getSurname(), user.getEmail(), user.getPassword());
    }
}


