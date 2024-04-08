package polling.app.polling.app.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({
    ElementType.FIELD,
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NonEmptyFieldsValidator.class)
public @interface NonEmptyFields {
    String message() default "Fields cannot be empty";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
