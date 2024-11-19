package pl.kurs.user.role.management.dto;

import pl.kurs.user.role.management.model.Role;

public record RoleDto(long id, String name) {
    public static RoleDto toDto(Role role) {
        return new RoleDto(role.getId(), role.getName());
    }
}
