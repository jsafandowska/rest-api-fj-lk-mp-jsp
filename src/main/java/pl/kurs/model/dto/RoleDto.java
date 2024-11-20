package pl.kurs.model.dto;

import pl.kurs.model.Role;

public record RoleDto(
        Long id,
        String name
) {
    public static RoleDto toDto(Role role) {
        return new RoleDto(
                role.getId(),
                role.getName()
        );
    }
}