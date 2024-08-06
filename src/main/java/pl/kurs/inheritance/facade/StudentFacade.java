package pl.kurs.inheritance.facade;

import org.springframework.stereotype.Service;
import pl.kurs.inheritance.dto.StudentDto;
import pl.kurs.inheritance.model.Student;

import java.util.Map;

@Service
public class StudentFacade implements PersonFacade<Student, StudentDto> {

    @Override
    public Student createPersonInternal(Map<String, String> parameters) {
        Student student = new Student();
        student.setName(parameters.get("name"));
        student.setGroup(parameters.get("group"));
        student.setAge(Integer.parseInt(parameters.get("age")));
        student.setScholarship(Integer.parseInt(parameters.get("scholarship")));
        return student;
    }

    @Override
    public StudentDto toDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getName(),
                student.getAge(),
                student.getScholarship(),
                student.getGroup()
        );
    }
}
