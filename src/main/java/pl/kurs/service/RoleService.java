package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pl.kurs.exceptions.RoleNotFoundException;
import pl.kurs.exceptions.UserNotFoundException;
import pl.kurs.model.Role;
import pl.kurs.model.User;
import pl.kurs.model.command.CreateRoleCommand;
import pl.kurs.repository.RoleRepository;
import pl.kurs.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role createRole(@RequestBody CreateRoleCommand command) {
        return roleRepository.saveAndFlush(new Role(command.getName()));
    }
}
