package contest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;


public class LoginServlet extends HttpServlet {	
    
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException{
		doPost(request,response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException
	{
		response.setContentType("text/html");
		//读取输入用户名和密码
		String username = request.getParameter("username");
		String pwd = request.getParameter("password");	
		Connection conn = null;			  
		Statement stmt = null;
		ResultSet rs = null;
		try {
			Object ds_obj = utils.ApplicationContextHelper
					.getBean("dataSource");
			conn = ((javax.sql.DataSource) ds_obj).getConnection();
			stmt=conn.createStatement();
			rs=stmt.executeQuery("select * from contest_user where username = '"+username+"' and password = '"+pwd+"'");
			
			if(rs.next()){
						
				HttpSession session = request.getSession();
				String auth=rs.getString("id")+"|"+rs.getString("role");
				Cookie cookie = new Cookie("AUTH",Base64.encodeBase64String(auth.getBytes()));
				response.addCookie(cookie);
				session.setAttribute("LOGIN", "true");
				
				response.sendRedirect("./Main");
			}else{			
				response.setContentType("text/html;charset=utf-8");
				response.getWriter().println("用户名或密码错误");
				response.setHeader("Refresh", "3; URL=./login.jsp");
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().println("数据库错误");
			e.printStackTrace();
		}finally{
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}
