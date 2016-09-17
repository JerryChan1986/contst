<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/contest";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <link rel="stylesheet" id="login-css" href="<%= basePath %>/css/login.css" type="text/css" media="all">
    <link rel="stylesheet" id="colors-fresh-css" href="<%= basePath %>/css/colors-fresh.css" type="text/css" media="all">
    <title>系统登录页面</title>
  </head>
  
  <body>
  <div id="login">                               
		<form name="loginform" id="loginform" action="./Login" method="post">
		<p>
			<label>用户名<br>
			<input type="text" name="username" id="user_login" class="input" value="" size="20" tabindex="10"></label>
		</p>
		<p>
			<label>密码<br>
			<input type="password" name="password" id="user_pass" class="input" value="" size="20" tabindex="20"></label>
		</p>
		<p class="forgetmenot"><label><input name="rememberme" type="checkbox" id="rememberme" value="forever" tabindex="90"> 记住我的登录信息</label></p>
		<p class="submit">
			<input type="submit" name="wp-submit" id="wp-submit" class="button-primary" value="登录" tabindex="100">
		</p>			
		</form>
  </div>
  </body>
</html>
