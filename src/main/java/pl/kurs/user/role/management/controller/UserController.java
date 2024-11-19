package pl.kurs.user.role.management.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.user.role.management.dto.UserDto;
import pl.kurs.user.role.management.model.command.CreateUserCommand;
import pl.kurs.user.role.management.service.UserService;

@RestController
@RequestMapping("api/v1/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUser(@PathVariable int id) {
        log.info("findUser({})", id);
        return ResponseEntity.status(HttpStatus.OK).body(UserDto.toDto(userService.findUser(id)));
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody CreateUserCommand command) {
        log.info("saveUser({})", command);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.toDto(userService.seveUser(command)));
    }

    @PostMapping("/{id}/roles/{roleId})")
    public ResponseEntity<UserDto> add(@PathVariable int id, @PathVariable int roleId) {
        log.info("addCar({}, {})", id, roleId);
        userService.addRoleToUser(id, roleId);
        return ResponseEntity.ok().build();
    }

}
