package martinbradley.auth0;

import com.auth0.client.auth.AuthAPI;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticationControllerProvider {
    private final String domain;
    private final String clientId;
    private final String clientSecret;
    private static Logger logger = LoggerFactory.getLogger(AuthenticationControllerProvider.class);

    public AuthenticationControllerProvider(ServletConfig config) {

        ServletContext servletContext = config.getServletContext();

        domain       = servletContext.getInitParameter("com.auth0.domain");
        clientId     = servletContext.getInitParameter("com.auth0.clientId");
        clientSecret = servletContext.getInitParameter("com.auth0.clientSecret");
    }

    public AuthAPI getAuthAPI() throws UnsupportedEncodingException {

        logger.debug("com.auth0.domain       " + domain);
        logger.debug("com.auth0.clientId     " + clientId);   
        logger.debug("com.auth0.clientSecret " + clientSecret);

        return new AuthAPI(domain, clientId, clientSecret);
                                     //.withResponseType("token")
                                     //.build();
    }
}
