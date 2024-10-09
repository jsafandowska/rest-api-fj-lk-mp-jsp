package pl.kurs.dictionary.model.command;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditDictionaryValueCommand {
    private String value;
}
