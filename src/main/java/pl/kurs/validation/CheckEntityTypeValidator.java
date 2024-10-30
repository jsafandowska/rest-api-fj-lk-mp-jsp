package pl.kurs.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import pl.kurs.inheritance.facade.PersonFacade;
import pl.kurs.validation.annotation.CheckEntityType;

import java.util.Map;

@RequiredArgsConstructor
public class CheckEntityTypeValidator implements ConstraintValidator<CheckEntityType, String> {

    private final Map<String, PersonFacade> facades;


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return facades.containsKey(value.toLowerCase() + "Facade");
    }
}
