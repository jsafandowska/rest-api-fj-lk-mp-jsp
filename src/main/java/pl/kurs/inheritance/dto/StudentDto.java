package pl.kurs.inheritance.dto;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.inheritance.model.Student;
@Getter
@Setter
public class StudentDto extends PersonDto {
    private int scholarship;
    private String group;

    public StudentDto(String name, int age, int scholarship, String group) {
        super(name, age);
        this.scholarship = scholarship;
        this.group = group;
    }

    public static StudentDto toDto(Student student) {
        return new StudentDto(
                student.getName(),
                student.getAge(),
                student.getScholarship(),
                student.getGroup()
        );
    }
}