package martinbradley.auth0;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import com.auth0.Tokens;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Servlet endpoint used as the callback handler in the OAuth 2.0 authorization code grant flow.
 * It will be called with the authorization code after a successful login.
 */
@WebServlet(urlPatterns = {"/auth0callback"})
public class CallbackServlet extends HttpServlet {

    private String redirectOnSuccess = "";// "https://localhost:3000/";
    private String redirectOnFail    = "";// "https://localhost:3000/loginfailure";
    private String auth0URL          = "";
    private String auth0Issuer       = "";
    private String auth0Namespace    = "";//"https://gorticrum.com/user_authorization"
    public static final String AUTH0_URL    = "AUTH0_URL";
    public static final String AUTH0_ISSUER = "AUTH0_ISSUER";
    public static final String AUTH0_NAMESPACE = "AUTH0_NAMESPACE";


    private AuthenticationController authenticationController;
    private static Logger logger = LoggerFactory.getLogger(CallbackServlet.class);
    private Auth0Verifier verifier;

    /**
     * Initialize this servlet with required configuration.
     * <p>
     * Parameters needed on the Local Servlet scope:
     * <ul>
     * <li>'com.auth0.redirect_on_success': where to redirect after a successful authentication.</li>
     * <li>'com.auth0.redirect_on_error': where to redirect after a failed authentication.</li>
     * </ul>
     * Parameters needed on the Local/Global Servlet scope:
     * <ul>
     * <li>'com.auth0.domain': the Auth0 domain.</li>
     * <li>'com.auth0.client_id': the Auth0 Client id.</li>
     * <li>'com.auth0.client_secret': the Auth0 Client secret.</li>
     * </ul>
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        final ServletContext context  = config.getServletContext(); 
        redirectOnSuccess = context.getInitParameter("AUTH0_LOGIN_SUCCESS");
        redirectOnFail    = context.getInitParameter("AUTH0_LOGIN_FAILURE");
        auth0Namespace    = context.getInitParameter("AUTH0_NAMESPACE");
        auth0Issuer       = context.getInitParameter(AUTH0_ISSUER);
        auth0URL          = context.getInitParameter(AUTH0_URL);
        
        try {
            authenticationController = new AuthenticationControllerProvider(config)
                                                            .getAuthController();

            Auth0KeyProvider provider = new Auth0KeyProvider(auth0URL);
            
            verifier = new Auth0Verifier(auth0Issuer, 
                                         provider.getPublicKey(null)); 
        }
        catch (com.auth0.jwk.JwkException e) {
            logger.warn("JwkException in init ", e);
            throw new ServletException("Couldn't create the AuthenticationController instance. Check the configuration.", e);
        }
        catch (UnsupportedEncodingException e) {
            throw new ServletException("Couldn't create the AuthenticationController instance. Check the configuration.", e);
        }
    }

//  /**
//   * Process a call to the redirect_uri with a GET HTTP method.
//   *
//   * @param req the received request with the tokens (implicit grant) or the authorization code (code grant) in the parameters.
//   * @param res the response to send back to the server.
//   * @throws IOException
//   * @throws ServletException
//   */
//  @Override
//  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
//      handle(req, res);
//  }


    /**
     * Process a call to the redirect_uri with a POST HTTP method. This occurs if the authorize_url included the 'response_mode=form_post' value.
     * This is disabled by default. On the Local Servlet scope you can specify the 'com.auth0.allow_post' parameter to enable this behaviour.
     *
     * @param req the received request with the tokens (implicit grant) or the authorization code (code grant) in the parameters.
     * @param res the response to send back to the server.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        handle(req, res);
    }

    private void handle(HttpServletRequest req, HttpServletResponse res) throws IOException {
        logger.debug("QueryString " +req.getQueryString());

        try {
            handleAuthToken(req, res);

            logger.debug("Success Redirecting to " + redirectOnSuccess);

            res.sendRedirect(redirectOnSuccess);

        } catch (Exception e) {
            /* Had been catching IdentityVerificationException 
             * But that exception cannot be created outside auth0 package.
             * So catching Exception to make it testable */
            logger.warn("Error in CallbackServlet '" + e.getMessage());
            logger.warn("Success Redirecting to " + redirectOnFail);
            res.sendRedirect(redirectOnFail);
        }
    }

    private void handleAuthToken(HttpServletRequest req,
                                 HttpServletResponse res) throws Exception {
        Tokens tokens = authenticationController.handle(req);
        String jSWebToken = tokens.getAccessToken();
        Set<String> groups = verifier.readGroups(jSWebToken, auth0Namespace);

        String groupsStr = groups.stream().collect(Collectors.joining(","));

        Cookie groupsCookie = new Cookie("auth0Groups", groupsStr);
        groupsCookie.setSecure(true);

        Cookie jwtCookie = new Cookie("jwtToken", jSWebToken);
        jwtCookie.setSecure(true);
        jwtCookie.setHttpOnly(true);

        res.addCookie(groupsCookie);
        res.addCookie(jwtCookie);
        logger.debug("Added Cookie " + groupsCookie);
        logger.debug("Added Cookie " + jwtCookie);


        // Between the login and the callback the http session
        // stores some data for auth0
        // after this callback is executed that state is no longer needed.
        // removing the session because for a restful frontend there
        // should be no client state stored.

        HttpSession session = req.getSession(false);

        if (session != null) {
            session.invalidate();
            logger.debug("Got rid of the session " + jwtCookie);
            // session only needed for the auth0 callback.
        }
    }
}
