package pl.kurs.dictionary.model.command;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDictionaryCommand {
    private String name;
    private Set<String> launchValues;
}
