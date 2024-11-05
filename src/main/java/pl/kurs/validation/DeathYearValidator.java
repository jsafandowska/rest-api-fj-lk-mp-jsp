package pl.kurs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.kurs.model.command.CreateAuthorCommand;
import pl.kurs.validation.annotation.CheckDeathYear;

import java.time.Year;

public class DeathYearValidator implements ConstraintValidator<CheckDeathYear, Integer> {

    @Override
    public void initialize(CheckDeathYear constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer deathYear, ConstraintValidatorContext context) {
        if (deathYear == null) {
            return true;
        }

        int currentYear = Year.now().getValue();
        boolean isValid = deathYear <= currentYear;

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("INVALID_DEATH_YEAR")
                    .addConstraintViolation();
        }

        return isValid;
    }
}