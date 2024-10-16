package pl.kurs.dictionary.model.command;

import lombok.*;

import java.util.Set;
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDictionaryValuesCommand {
        private int dictionaryId;
        private Set<String> values;
    }

