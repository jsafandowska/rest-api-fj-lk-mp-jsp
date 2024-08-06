package pl.kurs.inheritance.facade;

import org.springframework.stereotype.Service;
import pl.kurs.inheritance.dto.EmployeeDto;
import pl.kurs.inheritance.model.Employee;

import java.util.Map;

@Service
public class EmployeeFacade implements PersonFacade<Employee, EmployeeDto> {

    @Override
    public Employee createPersonInternal(Map<String, String> parameters) {
        Employee employee = new Employee();
        employee.setName(parameters.get("name"));
        employee.setPosition(parameters.get("position"));
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
                employee.getPosition(),
                employee.getSalary()
        );
    }

}