package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.model.Role;
import pl.kurs.model.dto.RoleDto;
import pl.kurs.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleDto createRole(String name) {
        Role role = new Role();
        role.setName(name);
        return RoleDto.toDto(roleRepository.save(role));
    }
}
