package pl.kurs.dictionary.model.command;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Getter
@Setter
@ToString
@Builder
public class CreateDictionaryCommand {
    private String name;
    private Set<String> launchValues;
}
