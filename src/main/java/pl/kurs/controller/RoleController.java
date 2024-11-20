package pl.kurs.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.exceptions.RoleNotFoundException;
import pl.kurs.exceptions.UserNotFoundException;
import pl.kurs.model.Role;
import pl.kurs.model.User;
import pl.kurs.model.command.CreateRoleCommand;
import pl.kurs.model.dto.RoleDto;
import pl.kurs.service.RoleService;

@RestController
@RequestMapping("/api/v1/roles")
@Slf4j
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleDto> createRole(@RequestBody CreateRoleCommand command){
        log.info("createRole({})",command);
        return ResponseEntity.status(HttpStatus.CREATED).body(RoleDto.toDto(roleService.createRole(command)));
    }

}
