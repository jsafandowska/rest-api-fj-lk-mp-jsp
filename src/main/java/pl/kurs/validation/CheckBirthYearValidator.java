package pl.kurs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.kurs.validation.annotation.CheckBirthYear;

import java.time.Year;

public class CheckBirthYearValidator implements ConstraintValidator<CheckBirthYear, Integer> {
    private static final int MIN_YEAR = 1800;
    @Override
    public boolean isValid(Integer birthYear, ConstraintValidatorContext context) {
        if (birthYear == null) {
            return false;
        }

        int currentYear = Year.now().getValue();
        return birthYear >= MIN_YEAR && birthYear <= currentYear;
    }

}
