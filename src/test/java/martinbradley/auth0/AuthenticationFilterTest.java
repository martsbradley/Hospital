package martinbradley.auth0;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.http.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;
import martinbradley.auth0.*;
import mockit.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Response;

public class AuthenticationFilterTest {
    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilterTest.class);
    AuthenticationFilter impl = new AuthenticationFilter();
    @Mocked ContainerRequestContext requestContext;
    @Mocked ServletContext servletContext;
    @Mocked ResourceInfo resourceInfo;
    @Mocked Auth0RSASolution auth0;
    @Mocked SecuredRestfulMethodHelper helper;

    @BeforeEach
    public void setMeUp() throws Exception {
        impl.resourceInfo = resourceInfo;
        impl.setServletContext(servletContext);
    }

    private void expectedAuth0Result(final boolean authResult) 
        throws IOException, ServletException {
        new Expectations() {{

            auth0.isValidAccessRequest(anyString, anyString, (String[])any);
            result = authResult;
        }};
    }

    private void expectBearerToken(final String aToken){
        new Expectations(){{
            requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            result = "Bearer " + aToken;
            logger.debug("expectBearerToken 'Bearer ' "+ aToken);
        }};
    }
    private void expectedGroups(String ... groups) throws Exception {
        new Expectations(){{
            helper.getGroups((ResourceInfo)any);
            result = groups;
        }};
    }

    private void timesAbortCalled(int aTimesCalled) {
        new Expectations(){{
            requestContext.abortWith( (Response)any);
            times = aTimesCalled;
        }};
    }

    @Test
    public void testSuccessful() 
        throws Exception {

        expectBearerToken("123");

        expectedGroups("admins");

        expectedAuth0Result(true);

        timesAbortCalled(0);

        new Expectations(){{
            requestContext.abortWith( (Response)any);
            times = 0;
        }};

        impl.filter(requestContext);
    }

    @Test
    public void testFailed_NoGroups() 
        throws Exception {

        expectBearerToken("123");

        expectedGroups();

        timesAbortCalled(1);

        impl.filter(requestContext);
    }
    @Test
    public void testFailed_NotAuthorized() 
        throws Exception {

        expectBearerToken("123");

        expectedGroups("admins");

        expectedAuth0Result(false);

        timesAbortCalled(1);

        impl.filter(requestContext);
    }
}
