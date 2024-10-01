package pl.kurs.inheritance.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.dictionary.model.DictionaryValue;
import pl.kurs.dictionary.repository.DictionaryValueRepository;
import pl.kurs.inheritance.dto.EmployeeDto;
import pl.kurs.inheritance.model.Employee;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeFacade implements PersonFacade<Employee, EmployeeDto> {
    private final DictionaryValueRepository dictionaryValueRepository;

    @Override
    public Employee createPersonInternal(Map<String, String> parameters) {
        DictionaryValue countryValue = dictionaryValueRepository.findByDictionaryNameAndValue("COUNTRIES", parameters.get("country"))
                .orElseThrow(() -> new IllegalStateException("Missing dictionary value in COUNTRIES"));
        DictionaryValue positionValue = dictionaryValueRepository.findByDictionaryNameAndValue("POSITIONS", parameters.get("position"))
                .orElseThrow(() -> new IllegalStateException("Missing dictionary value in POSITIONS"));
        Employee employee = new Employee();
        employee.setName(parameters.get("name"));
        employee.setPosition(positionValue);
        employee.setCountry(countryValue);
        employee.setAge(Integer.parseInt(parameters.get("age")));
        employee.setSalary(Integer.parseInt(parameters.get("salary")));
        return employee;
    }
    @Override
    public EmployeeDto toDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getAge(),
                employee.getCountry().getValue(),
                employee.getPosition().getValue(),
                employee.getSalary()
        );
    }

}