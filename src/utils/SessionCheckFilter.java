package utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class SessionCheckFilter implements Filter {

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response; 
		//res.setHeader("Access-Control-Allow-Origin", "*");//支持ajax跨域访问
		Object obj=req.getSession().getAttribute(WebConstants.USER_NAME);
		chain.doFilter(request, response);	
		
		String username="";
		String password="";
		
		
		String page=req.getServletPath();
		//if(page.indexOf("error.jsp")>0||page.indexOf("login.jsp")>0||page.indexOf("toLogin")>0){//error和login登陆页面不做过滤处理
		if(page.indexOf("error.jsp")>0||page.indexOf("toLogin")>0){//error和login登陆页面不做过滤处理
			chain.doFilter(request, response);
			return;
		}
		
	}

	
	public void init(FilterConfig arg0) throws ServletException {
	}
    
	
}
