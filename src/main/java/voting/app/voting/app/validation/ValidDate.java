package voting.app.voting.app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({
    ElementType.FIELD,
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidDateValidator.class)
public @interface ValidDate {
    String message() default "Invalid date";

    boolean isFuture() default false;

    boolean nullable() default false;

    String compareDate() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
