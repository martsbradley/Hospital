package martinbradley.auth0;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletConfig;

@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    private String domain   = "";
    private String clientId = "";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        domain = config.getServletContext().getInitParameter("com.auth0.domain");
        clientId = config.getServletContext().getInitParameter("com.auth0.clientId");
    }

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        System.out.println("LogoutServlet");
        if (request.getSession() != null) {
            request.getSession().invalidate();
            System.out.println("LogoutServlet invalidate called");
        }

        String returnTo = "https://localhost:3000/logoutsuccess";

        String logoutUrl = String.format("https://%s/v2/logout?client_id=%s&returnTo=%s", domain, clientId, returnTo);

        System.out.println("LogoutServlet redirect to '" + logoutUrl + "'");

        CookieHandler cookieHandler = new CookieHandler();
        cookieHandler.clearCookies(response);

        response.sendRedirect(logoutUrl);
    }
}
