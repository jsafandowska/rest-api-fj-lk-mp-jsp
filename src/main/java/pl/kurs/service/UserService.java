package pl.kurs.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kurs.exceptions.RoleNotFoundException;
import pl.kurs.exceptions.UserNotFoundException;
import pl.kurs.model.Role;
import pl.kurs.model.User;
import pl.kurs.model.command.CreateUserCommand;
import pl.kurs.repository.RoleRepository;
import pl.kurs.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public User createUser(CreateUserCommand command) {
        String encodedPassword = passwordEncoder.encode(command.getPassword());
        User user = new User(command.getName(), command.getSurname(), command.getEmail(), encodedPassword);
        return userRepository.saveAndFlush(user);
    }


    @Transactional(readOnly = true)
    public User findUser(int id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public void addRoleToUser(int userId, int roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(RoleNotFoundException::new);
        user.addRole(role);
        userRepository.saveAndFlush(user);
    }


}

