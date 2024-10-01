package pl.kurs.inheritance.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.dictionary.repository.DictionaryValueRepository;
import pl.kurs.inheritance.dto.StudentDto;
import pl.kurs.inheritance.model.Student;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentFacade implements PersonFacade<Student, StudentDto> {

    private final DictionaryValueRepository dictionaryValueRepository;

    @Override
    public Student createPersonInternal(Map<String, String> parameters) {
        DictionaryValue countryValue = dictionaryValueRepository.findByDictionaryNameAndValue("COUNTRIES", parameters.get("country"))
                .orElseThrow(() -> new IllegalStateException("Missing dictionary value in COUNTRIES"));
        Student student = new Student();
        student.setName(parameters.get("name"));
        student.setGroup(parameters.get("group"));
        student.setAge(Integer.parseInt(parameters.get("age")));
        student.setScholarship(Integer.parseInt(parameters.get("scholarship")));
        student.setCountry(countryValue);
        return student;
    }

    @Override
    public StudentDto toDto(Student student) {
        return new StudentDto(
                student.getId(),
                student.getName(),
                student.getAge(),
                student.getCountry().getValue(),
                student.getScholarship(),
                student.getGroup()
        );
    }
}
