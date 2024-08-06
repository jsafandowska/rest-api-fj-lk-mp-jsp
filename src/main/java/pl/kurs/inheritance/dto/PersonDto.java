package pl.kurs.inheritance.dto;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.inheritance.model.Person;
@Getter
@Setter
public class PersonDto {
    private String name;
    private int age;

    public PersonDto(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public static PersonDto toDto(Person person) {
        return new PersonDto(
                person.getName(),
                person.getAge()
        );
    }
}