package pl.kurs.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import pl.kurs.validation.CheckEntityTypeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckEntityTypeValidator.class)
public @interface CheckEntityType {
    String message() default "UNKNOWN_ENTITY_TYPE";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
