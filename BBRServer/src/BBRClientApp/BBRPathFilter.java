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

import BBRAcc.BBRPoS;
import BBRAcc.BBRPoSManager;

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
	    	HttpServletRequest request = (HttpServletRequest) req;
	    	HttpServletResponse response = (HttpServletResponse) res;
	
	    	String path = request.getRequestURI();
	    	String cont = request.getContextPath();
	    	
	    	if (path == null)
	    		path = "";
	    	
	    	if (cont == null)
	    		cont = "";
	    	
	    	if (!cont.isEmpty() && !path.isEmpty() && path.startsWith(cont)) {
	    		path = path.substring(cont.length());
	    	}
	    	
	    	String book = "/book";
	    	String planVisitPage = cont + "/general-plan-visit.jsp";
	    	
	    	if (path.startsWith(book)) {
	    		path = path.substring(book.length());
	    		if (path.startsWith("/")) {
	    			for (int i = 1; i <= 5; i++) {
		    			try {
			    			String posUrlID = path.substring(1);
			    			BBRPoSManager mgr = new BBRPoSManager();
			    			BBRPoS pos = mgr.findByUrlId(posUrlID);
			    			if (pos == null)
			    				response.sendRedirect(planVisitPage);
			    			else
			    				response.sendRedirect(planVisitPage + "?pos=" + pos.getId());
			    			break;
		    			} catch (Exception ex) {
		    				try {
								Thread.sleep(5000);
							} catch (InterruptedException e) {
							}
		    			}
	    			}
	    		} else
	    			response.sendRedirect(planVisitPage);
	    	} else
	        	chain.doFilter(request, response);
    }


    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {}

}
