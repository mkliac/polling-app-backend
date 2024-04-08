package polling.app.polling.app.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.Instant;

public class ValidDateValidator implements ConstraintValidator<ValidDate, Instant> {
    private boolean isFuture;

    private boolean nullable;

    private Instant compareDate;

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.isFuture = constraintAnnotation.isFuture();
        this.nullable = constraintAnnotation.nullable();
        if (constraintAnnotation.compareDate().isEmpty()) {
            this.compareDate = Instant.now();
        } else {
            this.compareDate = Instant.parse(constraintAnnotation.compareDate());
        }
    }

    @Override
    public boolean isValid(Instant instant, ConstraintValidatorContext constraintValidatorContext) {
        return instant == null && nullable
                || isFuture && instant.isAfter(compareDate)
                || !isFuture && instant.isBefore(compareDate);
    }
}
