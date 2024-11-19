package pl.kurs.user.role.management.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.user.role.management.dto.RoleDto;
import pl.kurs.user.role.management.dto.UserDto;
import pl.kurs.user.role.management.model.command.CreateRoleCommand;
import pl.kurs.user.role.management.model.command.CreateUserCommand;
import pl.kurs.user.role.management.service.RoleService;

@RestController
@RequestMapping("api/v1/roles")
@Slf4j
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping("/{id}")
    public ResponseEntity<RoleDto> findRole(@PathVariable int id) {
        log.info("findRole({})", id);
        return ResponseEntity.status(HttpStatus.OK).body(RoleDto.toDto(roleService.findRole(id)));
    }

    @PostMapping
    public ResponseEntity<RoleDto> saveRole(@RequestBody CreateRoleCommand command) {
        log.info("saveUser({})", command);
        return ResponseEntity.status(HttpStatus.CREATED).body(RoleDto.toDto(roleService.saveRole(command)));
    }
}