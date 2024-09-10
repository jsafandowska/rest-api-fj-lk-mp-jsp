package pl.kurs.pesimistic.lock.example.model.command;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVisitCommand {

    private String date;
    private Integer doctorId;
}
