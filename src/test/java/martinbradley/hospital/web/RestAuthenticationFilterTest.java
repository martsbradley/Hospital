package martinbradley.hospital.web;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import mockit.*;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.*;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;
import javax.ws.rs.core.HttpHeaders;

public class RestAuthenticationFilterTest {
    RestAuthenticationFilter impl = new RestAuthenticationFilter();
    @Mocked HttpServletRequest  request;
    @Mocked HttpServletResponse response;
    @Mocked FilterChain chain;

    @BeforeEach
    public void setMeUp() {
    }

    private void expectThatChainIsCalled() 
        throws IOException, ServletException {
        new Expectations() {{
            chain.doFilter(request, response);
        }};
    }

    private void expectBearerToken(final String aToken){
        new Expectations(){{
            request.getHeader(HttpHeaders.AUTHORIZATION);
            result = "Bearer " + aToken;
        }};
    }

    @Test
    public void testSuccessful() 
        throws IOException, ServletException {

        expectBearerToken("123");
        expectThatChainIsCalled();

        impl.doFilter(request, response, chain);
    }
}
