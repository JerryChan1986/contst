package contest;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.codec.binary.Base64;




public class MainServlet extends HttpServlet {

	
    
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException{
		
		response.setContentType("text/html; charset=UTF-8");
		response.setHeader("Cache-Control","no-cache");  
		response.setHeader("pragma","no-cache");  
		response.setDateHeader("expires",-1); 
		PrintWriter out = response.getWriter();		
		Connection conn = null;;			  
		Statement stmt = null;
		ResultSet rs = null;
		//读取身份认证信息
		Cookie cookies[] = request.getCookies();
		String USER_ID="",Role="";
		for(Cookie cookie:cookies){
			if(cookie.getName().equals("AUTH")){
				String auth= new String(Base64.decodeBase64(cookie.getValue()));
				USER_ID = auth.split("\\|")[0];
				Role = auth.split("\\|")[1];
			}
		}

		
		//读取用户操作
		String USER_ACTION = request.getParameter("action");
		
		//显示菜单
		out.println("<html><head><title>欢迎登录系统</title></head><body><h1 align=\"center\">欢迎登录系统</h1><div>");	 	
	 	out.println("<a href=\"./Main\">主页</a>");
	 	out.println("<a href=\"./Main?action=viewProfile\">我的信息</a>");	 	
	 	//管理员才可以访问的菜单
	 	if(Role!=null&&Role.equals("admin")){
	 		out.println("<a href=\"./Main?action=listAllUser\">用户管理</a>");
	 	}
	 	out.println("<a href=\"./Main?action=logout\">退出</a></div>");

	 	
	 	//主页
	 	if(USER_ACTION==null){
	 		out.println("<img src=\"./download.action?fileName=/images/banner_sm.png\" width=\"100%\" />");
	 	}
	 	
	 	//查询个人信息
	 	if(USER_ACTION!=null&&USER_ACTION.equals("viewProfile")){
	 		USER_ID=(request.getParameter("ID")!=null)?request.getParameter("ID"):USER_ID;
	 		if(USER_ID!=null&&!USER_ID.equals("")){	 			
	 			try {
	 				Object ds_obj = utils.ApplicationContextHelper
	 						.getBean("dataSource");
	 				conn = ((javax.sql.DataSource) ds_obj).getConnection();			  
	 				stmt=conn.createStatement();
	 				rs=stmt.executeQuery("select * from contest_user where id = "+USER_ID);	 				
	 				while(rs.next()) {
	 					String id = rs.getString("id");
	 					String username = rs.getString("username");
	 					String phone = rs.getString("phone");
	 					String address = rs.getString("address");
	 					String email = rs.getString("email");
	 					String avatar = rs.getString("avatar");
	 					String balance = rs.getString("balance").toString();
	 					if(avatar==null){
	 						avatar="./images/avatar.jpg";
	 					}
	 					out.println("<hr><form method=\"post\" action=\"./Main?action=modifyProfile\">");
	 					out.println("<tr><td width=\"80\"><b>头像:</b></td><td><img src="+avatar+" width=\"100\" heigth=\"100\" /></td></tr><br>");
	 					out.println("<tr><td width=\"80\"><b>ID:</b></td><td>"+id+"</td></tr><br>");
	 					out.println("<tr><td width=\"80\"><b>用户名:</b></td><td>"+username+"</td></tr><br>");
	 					out.println("<tr><td width=\"80\"><b>电话号码:</b></td><td><input type=\"text\" name=\"phone\" value=\""+phone+"\"></td></tr><br>");
	 					out.println("<tr><td width=\"80\"><b>地址:</b></td><td><input type=\"text\" name=\"address\" value=\""+address+"\"></td></tr><br>");
	 					out.println("<tr><td width=\"80\"><b>邮箱:</b></td><td><input type=\"text\" name=\"email\" value=\""+email+"\"></td></tr><br>");
	 					out.println("<tr><td width=\"80\"><b>余额:</b></td><td>"+balance+"</td></tr><br>");
	 					out.println("<input type=\"submit\" name=\"post\" value=\"确认提交\" />");
	 					out.println("<input type=\"button\" value=\"修改头像\" onclick=\"javascript:window.location.href='./fileupload.jsp?ID="+USER_ID+"'\" >");
	 					out.println("<input type=\"button\" value=\"修改密码\" onclick=\"javascript:window.location.href='./Main?action=modifyPassword'\" >");		 					
	 				}
	 				
	 			} catch (Exception e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}finally{
	 				try {
						stmt.close();
						rs.close();
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	 			}
	 		}
	 	}		
	 	
	 	//列举所有用户信息
		if(USER_ACTION!=null&&USER_ACTION.equals("listAllUser")){		 		
			try {
				Object ds_obj = utils.ApplicationContextHelper
						.getBean("dataSource");
				conn = ((javax.sql.DataSource) ds_obj).getConnection();			  
				stmt=conn.createStatement();
				rs=stmt.executeQuery("select * from contest_user where 1=1");				
				out.println("<table border=1 width=1100 cellspacing=0><tr><td width=\"5%\">ID</td><td width=\"15%\">用户名</td><td width=\"15%\">电话号码</td><td width=\"35%\">地址</td><td width=\"15%\">邮箱</td><td width=\"10%\">余额</td></tr>");	
				while(rs.next()) {
					String id = rs.getString("id");
					String username = rs.getString("username");
					String phone = rs.getString("phone");
					String address = rs.getString("address");
					String email = rs.getString("email");
					String balance = rs.getString("balance").toString();
					out.println("<tr><td>"+id+"</td><td>"+username+"</td><td>"+phone+"</td><td>"+address+"</td><td>"+email+"</td><td>"+balance+"</td><tr>");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				try {
					stmt.close();
					rs.close();
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
		//修改密码
	 	if(USER_ACTION!=null&&USER_ACTION.equals("modifyPassword")){		
	 		out.println("<hr><table><form method=\"post\" action=\"./Main?action=modifyPassword\">");
	 		out.println("<tr><td width=\"100\"><b>新密码:</b></td><td><input type=\"password\" name=\"passwd1\" value=\"\"</td></tr><br>");
	 		out.println("<tr><td width=\"100\"><b>确认新密码:</b></td><td><input type=\"password\" name=\"passwd2\" value=\"\"</td></tr><br></table>");
	 		out.println("<input type=\"submit\" name=\"post\" value=\"提交\" />");
	 		
	 	}

		//退出
		if(USER_ACTION!=null&&USER_ACTION.equals("logout")){
		 	out.println("安全退出中");
		 	response.setHeader("Refresh", "3; URL=./login.jsp");
		}
		out.println("</html>");
		out.close();
	 }
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException{
		
		response.setContentType("text/html; charset=UTF-8");
		response.setHeader("Cache-Control","no-cache");  
		response.setHeader("pragma","no-cache");  
		response.setDateHeader("expires",-1); 
		PrintWriter out = response.getWriter();
		Connection conn = null;			  
		Statement stmt = null;
		String sql = null;
				
		//读取身份认证信息
		Cookie cookies[] = request.getCookies();
		String USER_ID="";
		for(Cookie cookie:cookies){
			if(cookie.getName().equals("AUTH")){
				String auth= new String(Base64.decodeBase64(cookie.getValue()));
				USER_ID = auth.split("\\|")[0];
			}
		}
	 	
		//读取用户操作
		String USER_ACTION = request.getParameter("action");
				
		//修改信息
	 	if(USER_ACTION!=null&&USER_ACTION.equals("modifyProfile")){
	 		String phone = request.getParameter("phone");
	 		String address = request.getParameter("address");
	 		String email = request.getParameter("email");
	 		try {
	 			Object ds_obj = utils.ApplicationContextHelper
						.getBean("dataSource");
				conn = ((javax.sql.DataSource) ds_obj).getConnection();			  
	 			stmt=conn.createStatement();
	 			sql="update contest_user set phone='"+phone+"',address='"+address+"',email='"+email+"'where id = "+USER_ID;
	 			sql = new String(sql.getBytes(),"UTF-8");
	 			int rs=stmt.executeUpdate(sql);
	 			if(rs>0) {	 				
	 				out.println("更新成功！");
	 				response.setHeader("Refresh", "3; URL=./Main?action=viewProfile");
	 			}
	 		} catch (Exception e) {
	 			// TODO Auto-generated catch block
	 			e.printStackTrace();
	 		}finally{
	 			try {
					conn.close();
		 			stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 		}
	 	}
	 	
	 	//修改密码
	 	if(USER_ACTION!=null&&USER_ACTION.equals("modifyPassword")){
	 		String pwd1 = request.getParameter("passwd1");
	 		String pwd2 = request.getParameter("passwd2");
	 		if(pwd1.equals(pwd2)){
	 			try {
	 				Object ds_obj = utils.ApplicationContextHelper
	 						.getBean("dataSource");
	 				conn = ((javax.sql.DataSource) ds_obj).getConnection();			  
	 				stmt=conn.createStatement();
	 				sql="update contest_user set password='"+pwd1+"' where id = "+USER_ID;
	 				sql = new String(sql.getBytes(),"UTF-8");;
	 				int rs=stmt.executeUpdate(sql);
	 				if(rs>0) {
	 					out.println("修改成功！");
	 					response.setHeader("Refresh", "3; URL=./login.jsp");
	 				}
	 			} catch (Exception e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}finally{
	 				try {
						stmt.close();
						conn.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	 				
	 			}
	 		}else{
	 			out.println("输入密码不一致");
	 			response.setHeader("Refresh", "3; URL=./Main?action=modifyPassword");
	 		}
	 	}
	}
	 	
}