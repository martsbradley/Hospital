package martinbradley.auth0;

import javax.annotation.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import com.auth0.jwk.JwkException;
import javax.ws.rs.container.ResourceInfo;
import java.lang.reflect.Method;

@SecuredRestfulMethod
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String REALM = "example";
    private static final String AUTHENTICATION_SCHEME = "Bearer";
    private Auth0RSASolution auth0;
    private final String AUTH0_URL    = "AUTH0_URL";
    private final String AUTH0_ISSUER = "AUTH0_ISSUER";
    @Context private ResourceInfo resourceInfo;

    @Context
    public void setServletContext(ServletContext aContext) 
        throws JwkException {
        String auth0URL    = aContext.getInitParameter(AUTH0_URL);
        String auth0Issuer = aContext.getInitParameter(AUTH0_ISSUER);
        logger.warn("auth0URL    : " + auth0URL);
        logger.warn("auth0Issuer : " + auth0Issuer);
        auth0 = new Auth0RSASolution(auth0URL, auth0Issuer);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        String authorizationHeader =
                requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (!isTokenBasedAuthentication(authorizationHeader)) {
            logger.warn("JWT token missing.");
            abortWithUnauthorized(requestContext);
            return;
        }

        String token = authorizationHeader
                            .substring(AUTHENTICATION_SCHEME.length()).trim();
        try {
            if (validateToken(token) == false) {
                abortWithUnauthorized(requestContext);
            }

        } catch (Exception e) {
            logger.warn("Token invalid : ", e.getMessage());
            abortWithUnauthorized(requestContext);
        }
        logger.warn("Request AUTHORIZED");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {

        logger.warn("Aborting with UNAUTHORIZED");
        // Abort the filter chain with a 401 status code response
        // The WWW-Authenticate header is sent along with the response
        requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, 
                                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
                        .build());
    }

    private boolean isTokenBasedAuthentication(String authorizationHeader) {

        // Check if the Authorization header is valid
        // It must not be null and must be prefixed with "Bearer" plus a whitespace
        // The authentication scheme comparison must be case-insensitive
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                    .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private boolean validateToken(String token) throws Exception {

        Method method = resourceInfo.getResourceMethod();
        String[] scopes = new String [0];

        if (method != null) {
            SecuredRestfulMethod secured = method.getAnnotation(SecuredRestfulMethod.class);
            scopes =  secured.scopes();  
            logger.warn("annotation scope is: " + scopes);  
        }

        boolean isValid =  auth0.canTokenAccess(token, scopes);
        logger.warn("Validated token as :" + isValid);
        return isValid;
    }
}
