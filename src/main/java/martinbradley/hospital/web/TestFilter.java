package martinbradley.hospital.web;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.HttpHeaders;
import javax.servlet.http.HttpFilter;

public class TestFilter extends HttpFilter {

   protected FilterConfig filterConfig;

   private static Logger logger = LoggerFactory.getLogger(TestFilter.class);
   
   @Override
   public void init(FilterConfig filterConfig) throws ServletException {
      this.filterConfig = filterConfig;

      logger.warn("init() called");
   }


   @Override
   public void doFilter(HttpServletRequest request, 
                        HttpServletResponse response, 
                        FilterChain chain) throws java.io.IOException, ServletException {

      logger.warn("TestFilter RequestURI :" + request.getRequestURI());
      chain.doFilter(request, response);
    }
}
