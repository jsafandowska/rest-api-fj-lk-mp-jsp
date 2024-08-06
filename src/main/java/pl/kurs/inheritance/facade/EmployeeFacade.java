package pl.kurs.inheritance.facade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.inheritance.dto.EmployeeDto;
import pl.kurs.inheritance.dto.PersonDto;
import pl.kurs.inheritance.model.Employee;
import pl.kurs.inheritance.model.PersonParameter;
import pl.kurs.inheritance.repository.EmployeeRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeFacade implements PersonFacade {

    private final EmployeeRepository employeeRepository;

    @Override
    public PersonDto addPerson(List<PersonParameter> parameters) {
        EmployeeDto employeeDto = mapToEmployeeDto(parameters);
        Employee employee = new Employee(
                employeeDto.getName(),
                employeeDto.getAge(),
                employeeDto.getPosition(),
                employeeDto.getSalary()
        );
        employee = employeeRepository.save(employee);
        return EmployeeDto.toDto(employee);
    }

    private EmployeeDto mapToEmployeeDto(List<PersonParameter> parameters) {
        String name = getParameterValue(parameters, "name");
        int age = Integer.parseInt(getParameterValue(parameters, "age"));
        String position = getParameterValue(parameters, "position");
        int salary = Integer.parseInt(getParameterValue(parameters, "salary"));
        return new EmployeeDto(name, age, position, salary);
    }

    private String getParameterValue(List<PersonParameter> parameters, String name) {
        return parameters.stream()
                .filter(param -> param.getName().equalsIgnoreCase(name))
                .map(param -> param.getValue().toString())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Parameter missing: " + name));
    }
}