package pl.kurs.user.role.management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.user.role.management.exception.RoleNotFoundException;

import pl.kurs.user.role.management.model.Role;
import pl.kurs.user.role.management.model.command.CreateRoleCommand;
import pl.kurs.user.role.management.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;

    public Role saveRole(CreateRoleCommand command) {
        return roleRepository.saveAndFlush(new Role(command.getName()));
    }


    @Transactional(readOnly = true)
    public Role findRole(int id) {
        return roleRepository.findById(id).orElseThrow(RoleNotFoundException::new);
    }
}
