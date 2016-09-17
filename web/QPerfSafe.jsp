<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>  

<%
response.addHeader("location", "./contest/login.jsp");
response.setStatus(302);
%>

