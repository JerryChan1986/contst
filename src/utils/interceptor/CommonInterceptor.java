package utils.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class CommonInterceptor implements HandlerInterceptor {

	private String mappingURL;//利用正则映射到需要拦截的路径    
    public void setMappingURL(String mappingURL) {    
           this.mappingURL = mappingURL;    
   }   
    
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		
	}

	/**
	 * spring mvc controller 执行前处理逻辑
	 * 暂未处理
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		String url=request.getRequestURL().toString();  
		Object userName=request.getSession().getAttribute("user_name");
		Object key=request.getParameter("key");
       /* if(mappingURL==null || url.matches(mappingURL)){    
            request.getRequestDispatcher("/error.jsp").forward(request, response);  
            return false;   
        }    */
        return true;  
	}

}
