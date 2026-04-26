package filters;


import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet Filter implementation class Filter
 */
@WebFilter("/*")
public class AuthFilter extends HttpFilter implements jakarta.servlet.Filter {
       
	/**
	 * @see AuthFilter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req= (HttpServletRequest) request;
		HttpServletResponse res= (HttpServletResponse) response;
		
		String servletPath = req.getServletPath();

		if (servletPath.startsWith("/login") || servletPath.startsWith("/register")) {
		    chain.doFilter(request, response);
		    return;
		}
		
		HttpSession session = req.getSession(false);
		
        if (session != null && session.getAttribute("userId") != null) {
        	// pass the request along the filter chain
    		chain.doFilter(request, response);
        }
        else
        {
        	res.sendRedirect("login");
        }

	}

	/**
	 * @see AuthFilter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
