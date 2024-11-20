package pl.kurs.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.RoleNotFoundException;
import pl.kurs.exceptions.UserNotFoundException;
import pl.kurs.model.Role;
import pl.kurs.model.User;
import pl.kurs.model.command.CreateUserCommand;
import pl.kurs.model.dto.UserDto;
import pl.kurs.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserCommand command) {
        log.info("addUserCreateUserCommand({})", command);
        User createdUser = userService.createUser(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUser(@PathVariable int id) {
        log.info("findUser({id})", id);
        return ResponseEntity.ok(UserDto.toDto(userService.findUser(id)));
    }

    @PatchMapping("/{id}/roles/{rolesId}")
    public ResponseEntity<UserDto> addRoleToUser(@PathVariable int id, @PathVariable int roleId) {
        log.info("addRoleToUser({}, {})", id, roleId);
        userService.addRoleToUser(id, roleId);
        return ResponseEntity.ok().build();
    }


}
