package pl.kurs.model.dto;

import pl.kurs.model.User;

public record UserDto(int id, String name, String surname, String email, String password) {

    public static UserDto toDto(User user) {
        return new UserDto(user.getId(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getPassword());
    }


}

