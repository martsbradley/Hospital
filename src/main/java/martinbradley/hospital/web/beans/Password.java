package martinbradley.hospital.web.beans;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import javax.validation.ConstraintValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.ConstraintValidatorContext;
import martinbradley.hospital.web.valid.PasswordsMatch;

@Named(value="passwordBean")
@RequestScoped
@PasswordsMatch(groups=java.util.RandomAccess.class)
public class Password 
{
    private static final Logger logger = LoggerFactory.getLogger(Password.class);

    @Size(min = 1)
    private String name;

    @Size(min = 1)
    private String surname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String check() 
    {
        logger.warn(String.format("Check called '%s', '%s'", name,surname));
        return "greeting";
    }
}
