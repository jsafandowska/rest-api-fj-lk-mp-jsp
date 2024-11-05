package pl.kurs.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.kurs.model.command.CreateAuthorCommand;

public class DeathYearValidator implements ConstraintValidator<CheckDeathYear, CreateAuthorCommand> {

    @Override
    public void initialize(CheckDeathYear constraintAnnotation) {
    }

    @Override
    public boolean isValid(CreateAuthorCommand command, ConstraintValidatorContext context) {
        if (command.getBirthYear() == null || command.getDeathYear() == null) {
            return true;
        }
        return command.getDeathYear() >= command.getBirthYear();
    }
}