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
import BBRClientApp.BBRApplication;

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
    	BBRApplication app = BBRApplication.getApp(request);

    	String path = request.getRequestURI();
    	if (path.startsWith(request.getContextPath() + "/general-signin.jsp")) {
    		chain.doFilter(request, response);
    	} else
        if (app.user == null) {
        	//chain.doFilter(request, response);
        	response.sendRedirect(request.getContextPath() + "/general-signin.jsp");
        } else {
            chain.doFilter(request, response);
        }
    }


    /**
     * @see Filter#destroy()
     */
    @Override
    public void destroy() {}

}
