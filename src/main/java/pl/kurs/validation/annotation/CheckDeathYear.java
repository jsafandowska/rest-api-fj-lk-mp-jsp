package pl.kurs.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.kurs.validation.DeathYearValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DeathYearValidator.class)
public @interface CheckDeathYear {
    String message() default "INVALID_DEATH_YEAR";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
