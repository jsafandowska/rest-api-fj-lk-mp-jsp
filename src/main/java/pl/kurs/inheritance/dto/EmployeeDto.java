package pl.kurs.inheritance.dto;
import lombok.Getter;
import lombok.Setter;
import pl.kurs.inheritance.model.Employee;
@Getter
@Setter
public class EmployeeDto extends PersonDto {
    private String position;
    private int salary;

    public EmployeeDto(String name, int age, String position, int salary) {
        super(name, age);
        this.position = position;
        this.salary = salary;
    }

    public static EmployeeDto toDto (Employee employee){
        return new EmployeeDto(
                employee.getName(),
                employee.getAge(),
                employee.getPosition(),
                employee.getSalary()
        );
    }
    }

