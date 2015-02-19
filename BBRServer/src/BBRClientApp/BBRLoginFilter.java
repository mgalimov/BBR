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
import BBRClientApp.BBRContext;

public class BBRLoginFilter implements Filter {

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
    	BBRContext context = BBRContext.getContext(request);

    	String path = request.getRequestURI();
    	String page = path;
    	
    	if (path.startsWith(request.getContextPath() + "/"))
    		 page = path.substring(request.getContextPath().length() + 1, path.length());
    	
    	if (page.startsWith(context.getSignInPage())) {
    		chain.doFilter(request, response);
    	} else
        if (!context.isPageAllowed(page)){
        	response.sendRedirect(context.getWelcomePage());
        } else
        	chain.doFilter(request, response);
    }


    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {}

}
