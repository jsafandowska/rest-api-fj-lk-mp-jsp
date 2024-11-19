package pl.kurs.user.role.management.model.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateUserCommand {
    private String name;
    private String surname;
    private String email;
    private String password;
}
