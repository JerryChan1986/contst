package contest;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CheckloginFilter implements Filter {


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest request = (HttpServletRequest) servletRequest;    
		HttpServletResponse response = (HttpServletResponse) servletResponse;    
		   
		HttpSession session = request.getSession(); 
		Object logined = session.getAttribute("LOGIN");
		//设置登录标识
		if (logined != null && logined.equals("true")) {
		        chain.doFilter(request, response);
		    } else {
		    	response.sendRedirect("./login.jsp");
		    }
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}

