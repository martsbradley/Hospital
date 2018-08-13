package martinbradley.hospital.web.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordsMatchImpl.class)
@Documented
public @interface PasswordsMatch
{
    String message() default "{constraints.passwords.match}";

    Class<?>[] groups() default {};


    Class<? extends Payload>[] payload() default {};
}
