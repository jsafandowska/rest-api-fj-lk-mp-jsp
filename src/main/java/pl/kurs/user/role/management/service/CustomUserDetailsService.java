package pl.kurs.user.role.management.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.kurs.user.role.management.repository.UserRepository;

@Service
    public class CustomUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

        public CustomUserDetailsService(UserRepository userRepository) {
            this.userRepository = userRepository;
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            pl.kurs.user.role.management.model.User user = userRepository.findByEmailWithRoles(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            String[] roles = user.getRoles().stream()
                    .map(role -> role.getName().toUpperCase()) // Role powinny mieć prefiks ROLE_
                    .toArray(String[]::new);
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(roles)
                    .build();
        }
    }

