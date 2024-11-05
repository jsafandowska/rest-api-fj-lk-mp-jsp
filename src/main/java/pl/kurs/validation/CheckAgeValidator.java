package pl.kurs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import pl.kurs.validation.annotation.CheckAge;
@RequiredArgsConstructor
public class CheckAgeValidator implements ConstraintValidator<CheckAge, Integer> {

    @Override
    public void initialize(CheckAge constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer age, ConstraintValidatorContext context) {
        if (age == null) {
            return true;
        }

        boolean isValid = age >= 0 && age <= 120;

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("INVALID_AGE")
                   .addConstraintViolation();
        }

        return isValid;
    }
}
