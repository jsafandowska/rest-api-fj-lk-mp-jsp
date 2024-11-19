package pl.kurs.user.role.management.model.command;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateRoleCommand {
    private String name;
}
