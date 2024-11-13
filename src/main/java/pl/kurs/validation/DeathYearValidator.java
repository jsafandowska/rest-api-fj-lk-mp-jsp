package pl.kurs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.kurs.validation.annotation.CheckDeathYear;

import java.time.Year;

public class DeathYearValidator implements ConstraintValidator<CheckDeathYear, Integer> {

    @Override
    public boolean isValid(Integer deathYear, ConstraintValidatorContext context) {
        int currentYear = Year.now().getValue();
        return deathYear <= currentYear;
    }
}