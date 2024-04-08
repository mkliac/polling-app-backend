package polling.app.polling.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collection;

public class NonEmptyFieldsValidator
        implements ConstraintValidator<NonEmptyFields, Collection<String>> {
    @Override
    public void initialize(NonEmptyFields constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(
            Collection<String> strings, ConstraintValidatorContext constraintValidatorContext) {
        return strings.stream().allMatch(s -> s != null && !s.isEmpty());
    }
}
