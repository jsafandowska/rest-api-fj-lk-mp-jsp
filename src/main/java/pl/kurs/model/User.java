package pl.kurs.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "api_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @ToString.Exclude
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_roles", // nazwa tabeli pośredniej
            joinColumns = @JoinColumn(name = "user_id"), // klucz obcy do tabeli User
            inverseJoinColumns = @JoinColumn(name = "role_id") // klucz obcy do tabeli Role
            )
    private Set<Role> roles = new HashSet<>();
}
