package BBRClientApp;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BBRPathFilter implements Filter {

    /**
     * @see Filter#init(FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
    	/*try*/ {
	    	HttpServletRequest request = (HttpServletRequest) req;
	    	HttpServletResponse response = (HttpServletResponse) res;
	
	    	String path = request.getPathInfo(); //request.getRequestURI();
	    	
	    	if (path.startsWith("/book")) {
	    		response.sendRedirect("general-plan-visit.jsp");
	    	} else
	        	chain.doFilter(request, response);
    	} /*catch (Exception ex) {
    		HttpServletResponse response = (HttpServletResponse) res;
    		response.sendRedirect("general-error.jsp");    		
    	}*/
    }


    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {}

}
