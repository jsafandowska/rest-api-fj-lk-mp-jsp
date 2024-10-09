package pl.kurs.dictionary.model.command;


import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDictionaryValueCommand {
    private String value;

}