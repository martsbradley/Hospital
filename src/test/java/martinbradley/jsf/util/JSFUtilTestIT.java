package martinbradley.jsf.util;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import javax.transaction.UserTransaction;
import martinbradley.hospital.core.domain.Patient;
import martinbradley.hospital.core.domain.SavePatientResponse;
import mockit.*;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import martinbradley.hospital.core.api.dto.MessageCollection;
import java.util.Locale;
import javax.faces.context.FacesContext;

public class JSFUtilTestIT
{
    @Injectable MessageCollection messages;
    @Injectable UIOutput reportErrors;
    @Tested JSFUtil impl;
    
    @Test
    public void when_no_errors_return_false(@Mocked FacesContext facesctx,
                                            @Mocked final UIViewRoot root)
    {
        new Expectations(){{
            messages.hasMessages(); result = false;

        }};

        boolean result = impl.handleErrors(messages, reportErrors);

        assertThat(result, is(false));
    }
    @Test
    public void when_errors_return_true(@Mocked FacesContext facesctx,
                                        @Mocked final UIViewRoot root)
    {
        new Expectations(){{
            messages.hasMessages(); result = true;

            facesctx.getViewRoot(); result = root;
            root.getLocale(); result = Locale.UK;
        }};

        boolean result = impl.handleErrors(messages, reportErrors);

        assertThat(result, is(true));
    }
}
