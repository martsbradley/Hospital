package martinbradley.hospital.web;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpFilter;
import martsbradley.auth0.Auth0Verifier;
import martinbradley.auth0.Auth0KeyProvider;
import com.auth0.jwk.JwkException;
import java.security.interfaces.RSAPublicKey;

public class RestAuthenticationFilter extends HttpFilter {
    protected FilterConfig filterConfig;

    private static Logger logger = LoggerFactory.getLogger(RestAuthenticationFilter.class);
   
    private final String AUTH0_URL    = "AUTH0_URL";
    private final String AUTH0_ISSUER = "AUTH0_ISSUER";
    private static final String AUTH0_BEARER = "Bearer ";

    private String auth0URL = "";
    private String auth0Issuer = "";
    private Auth0Verifier auth0Verifier;

    public void init(FilterConfig filterConfig) 
        throws ServletException {
        this.filterConfig = filterConfig;

        logger.warn("init() called");
        readConfig();
        initAuth0Verifier();
    }

    private void readConfig() {
        if (filterConfig != null) {
            logger.warn("readConfig has a " + filterConfig);

            auth0URL = filterConfig.getInitParameter(AUTH0_URL);
            auth0Issuer = filterConfig.getInitParameter(AUTH0_ISSUER);
        }
    }

    private void initAuth0Verifier()
        throws ServletException{
        if (auth0Verifier == null) {
            logger.info("Creating Auth0Verifier");

            try {
                Auth0KeyProvider provider = new Auth0KeyProvider(auth0URL);

                auth0Verifier = new Auth0Verifier(auth0Issuer, 
                                                  (RSAPublicKey)provider.getPublicKey(null));
            }
            catch (JwkException  e) {
                logger.warn("Problem creating Auth0Verifier :", e);
                throw new ServletException(e);
            }
        }
    }

    public void doFilter(HttpServletRequest request, 
                        HttpServletResponse response, 
                        FilterChain chain) 
            throws java.io.IOException, ServletException {

        logger.warn("Requesting :" + request.getRequestURI());

        String bearerToken = obtainBearerToken(request);

        if (isBearerTokenValid(bearerToken)) {
            chain.doFilter(request, response);
        }
    }

    private String obtainBearerToken(HttpServletRequest request) {
        logger.warn("Looking for "+ HttpHeaders.AUTHORIZATION);

        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (bearerToken != null  && 
            bearerToken.startsWith(AUTH0_BEARER) && 
            bearerToken.length() > AUTH0_BEARER.length()) {
            String jwt = bearerToken.substring(AUTH0_BEARER.length(), 
                                               bearerToken.length());
            logger.warn("bearerToken " + jwt);
            return jwt;
        }

        logger.warn("No bearer token found");
        return "";
    }

    private boolean isBearerTokenValid(final String aBearerToken){
        boolean isValid = auth0Verifier.isTokenValid(aBearerToken);
        return isValid;
        //return "123".equals(aBearerToken);
    }
}
