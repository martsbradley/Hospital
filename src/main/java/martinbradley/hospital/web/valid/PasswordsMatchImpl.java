package martinbradley.hospital.web.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import martinbradley.hospital.web.beans.Password;

public class PasswordsMatchImpl 
    implements ConstraintValidator<PasswordsMatch, Password>
{
    private static final Logger logger = LoggerFactory.getLogger(PasswordsMatchImpl.class);

    @Override
    public void initialize(final PasswordsMatch constraintAnnotation)
    {
    }

    @Override
    public boolean isValid(final Password form, 
                           final ConstraintValidatorContext context)
    {
        logger.info("Running the validation");
        String name = form.getName();
        String surname = form.getSurname();

        boolean matches  = name.equals(surname);
        logger.info(String.format("'%s' matches '%s'",name,surname));
        return matches;
    }
}
