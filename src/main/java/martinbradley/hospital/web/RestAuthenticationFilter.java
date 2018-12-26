package martinbradley.hospital.web;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpFilter;

public class RestAuthenticationFilter extends HttpFilter {

   protected FilterConfig filterConfig;

   private List revokeList; 
   private static Logger logger = LoggerFactory.getLogger(RestAuthenticationFilter.class);
   
    public void init(FilterConfig filterConfig) 
        throws ServletException {
        this.filterConfig = filterConfig;

        logger.warn("init() called");
        revokeList = new java.util.ArrayList(); 
        readConfig();
    }

    public void destroy() {
        this.filterConfig = null;
        revokeList = null;
        logger.warn("destroy() called");
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

    private void readConfig() {
    if ( filterConfig != null ) {
        logger.warn("readConfig has a " + filterConfig);

        //   // get the revoked user list file and open it.
        //   BufferedReader in;
        //   try { 
        //         String filename = filterConfig.getInitParameter("RevokedUsers");
        //         in = new BufferedReader( new FileReader(filename));
        //   } catch ( FileNotFoundException fnfe) {
        //         return;
        //   }
        //  
        //   // read all the revoked users and add to revokeList. 
        //   String userName;
        //   try {
        //         while ( (userName = in.readLine()) != null ) 
        //             revokeList.add(userName);
        //   } catch (IOException ioe) {
        //       logger.warn("ioe exception");
        }
    }

    private static final String BEARER = "Bearer ";


    private String obtainBearerToken(HttpServletRequest request) {
        logger.warn("Looking for "+ HttpHeaders.AUTHORIZATION);

        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (bearerToken != null  && 
            bearerToken.startsWith(BEARER) && 
            bearerToken.length() > BEARER.length()) {
            String jwt = bearerToken.substring(BEARER.length(), 
                                               bearerToken.length());
            logger.warn("bearerToken " + jwt);
            return jwt;
        }

        logger.warn("No bearer token found");
        return "";
    }

    private boolean isBearerTokenValid(final String aBearerToken){
        return "123".equals(aBearerToken);
    }
}
