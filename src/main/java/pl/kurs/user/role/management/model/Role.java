package pl.kurs.user.role.management.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.Set;
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(mappedBy = "roles")
    @ToString.Exclude
    private Set<User> users;
    public Role(String name) {
        this.name = name;
    }

}

