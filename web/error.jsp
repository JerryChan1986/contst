<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>主页</title>
<link href="<%= basePath %>js/bootstrap3/css/bootstrap.min.css" rel="stylesheet">
<script src="<%= basePath %>js/jquery-1.11.1.min.js"></script>
<script src="<%= basePath %>js/bootstrap3/js/bootstrap.min.js"></script>

</head>
<body>

<div class="container-fluid" style="padding-left:0px;">
  <div class="row" style="padding-left:20px;">
    <h4>抱歉，打开页面错误。请重新操作。</h4>
  </div>
  <div class="row" style="padding-left:20px;">
    <%
       out.println(request.getAttribute("error"));
     %>
  </div>

</div>	
</body>
</html>