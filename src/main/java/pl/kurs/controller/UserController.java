package pl.kurs.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.model.Role;
import pl.kurs.model.User;
import pl.kurs.model.command.CreateRoleCommand;
import pl.kurs.model.command.CreateUserCommand;
import pl.kurs.model.dto.RoleDto;
import pl.kurs.model.dto.UserDto;
import pl.kurs.service.RoleService;
import pl.kurs.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    // Tworzenie nowego użytkownika
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody CreateUserCommand request) {
        UserDto user = userService.createUser(request.getName(), request.getSurname(), request.getEmail(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    // Przypisywanie roli do użytkownika
    @PostMapping("/{id}/roles/{roleName}")
    public ResponseEntity<Void> assignRole(@PathVariable Long id, @PathVariable String roleName) {
        userService.assignRoleToUser(id, roleName);
        return ResponseEntity.ok().build();
    }

    // Pobieranie użytkownika po ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // Tworzenie nowej roli
    @PostMapping("/roles")
    public ResponseEntity<RoleDto> createRole(@RequestBody CreateRoleCommand request) {
        RoleDto role = roleService.createRole(request.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }
}