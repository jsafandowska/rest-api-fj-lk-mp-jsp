package pl.kurs.user.role.management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.exceptions.CarNotFoundException;
import pl.kurs.exceptions.GarageNotFoundException;
import pl.kurs.model.Car;
import pl.kurs.model.Garage;
import pl.kurs.user.role.management.exception.RoleNotFoundException;
import pl.kurs.user.role.management.exception.UserNotFoundException;
import pl.kurs.user.role.management.model.Role;
import pl.kurs.user.role.management.model.User;
import pl.kurs.user.role.management.model.command.CreateUserCommand;
import pl.kurs.user.role.management.repository.RoleRepository;
import pl.kurs.user.role.management.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public User seveUser(CreateUserCommand command) {
        return userRepository.saveAndFlush(new User((command.getName()), command.getSurname(), command.getEmail(), command.getPassword()));
    }


    @Transactional(readOnly = true)
    public User findUser(int id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }


    public void addRoleToUser(int id, int roleId) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException());
        user.getRoles().add(role);
        role.getUsers().add(user);
       userRepository.saveAndFlush(user);
    }
}
