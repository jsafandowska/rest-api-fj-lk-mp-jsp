package pl.kurs.inheritance.facade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.inheritance.dto.PersonDto;
import pl.kurs.inheritance.dto.StudentDto;
import pl.kurs.inheritance.model.PersonParameter;
import pl.kurs.inheritance.model.Student;
import pl.kurs.inheritance.repository.StudentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentFacade implements PersonFacade {

    private final StudentRepository studentRepository;

    @Override
    public PersonDto addPerson(List<PersonParameter> parameters) {
        StudentDto studentDto = mapToStudentDto(parameters);
        Student student = new Student(
                studentDto.getName(),
                studentDto.getAge(),
                studentDto.getScholarship(),
                studentDto.getGroup()
        );
        student = studentRepository.save(student);
        return StudentDto.toDto(student);
    }

    private StudentDto mapToStudentDto(List<PersonParameter> parameters) {
        String name = getParameterValue(parameters, "name");
        int age = Integer.parseInt(getParameterValue(parameters, "age"));
        int scholarship = Integer.parseInt(getParameterValue(parameters, "scholarship"));
        String group = getParameterValue(parameters, "group");
        return new StudentDto(name, age, scholarship, group);
    }

    private String getParameterValue(List<PersonParameter> parameters, String name) {
        return parameters.stream()
                .filter(param -> param.getName().equalsIgnoreCase(name))
                .map(param -> param.getValue().toString())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Parameter missing: " + name));
    }
}
