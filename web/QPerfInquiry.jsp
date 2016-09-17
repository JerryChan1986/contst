<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>  
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>2016性能调优联机题目</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="X-UA-Compatible" content="IE=edge"> 
  	<link href="./js/bootstrap3/css/bootstrap.min.css" rel="stylesheet">
<script src="./js/jquery-1.11.1.min.js"></script>
<script src="./js/bootstrap3/js/html5shiv.min.js"></script>
<script src="./js/bootstrap3/js/respond.min.js"></script>
<script src="./js/bootstrap3/js/bootstrap.min.js"></script>
	
  </head>
  
  <body>
  
  
<div class="container">    

<div class="row" >
    <div class="panel panel-default">
      <div class="panel-heading"><b>查询</b></div>
      <form action="./exam/QPerfInquiry.do"  method="post" >
      <div class="panel-body">
        <label>查询2015年上半年浦东新区销售总额前10名商户信息</label><br>

    开始日期  <input type="text" id="startDate" name="startMonth" class="form-control" value="20150101" readonly onclick="alert('调优效果需要,日期已填好，无需编辑！')"><br>
    结束日期 <input type="text" id="endDate" name="endMonth" class="form-control" value="20150630" readonly onclick="alert('调优效果需要,日期已填好，无需编辑！')">
    
    <br>
       <label>请选择区（请选择浦东新区）</label>     
       <select class="col-sm-10form-control" name="discID" id="discID">
          <option value="0">请选择地区</option>
           <option value="1"  selected>浦东新区</option>
           <option value="2">杨浦区</option>
           <option value="3">闸北区</option> 
       </select>
       <button type="submit" class="btn btn-sm btn-info" style="float: right;margin-top:10px;margin-right: 10px;">查询</button>
  	 
  	  </div>
  	  </form>
    </div>
</div>

<%
   List<Map<String,Object>> data_inquiry=(List<Map<String,Object>>)request.getAttribute("data_inquiry");
   if(data_inquiry!=null && data_inquiry.size()>0){
%>

<div class="row" id="inquirydiv"  >
    <div class="panel panel-default">
      <div class="panel-heading"><b>2015年所选地区上半年销售额前<%out.print(data_inquiry.size()); %>名商户列表如下</b></div>
      <table class="table">
		        <thead>
		          <tr>
		            <th>序号</th><th>商户ID</th><th>商户名称</th><th>交易总额</th>
		          </tr>
		        </thead>
		        <tbody>
<%  	for(int i=0;i<data_inquiry.size();i++){
	        Map<String,Object> row=data_inquiry.get(i);
	        out.println("<tr><th>"+row.get("seq")+"</th><td>"+row.get("store_id")+"</td><td>"+row.get("store_name")+"</td><td>"+row.get("total_amount")+"</td></tr>");
	    } 
%>
	             </tbody>
	  	</table>
 		</div>
	</div> 
<%   }%>
		        </tbody>
	  </table>
     
    </div>
</div>
 <button type="button"" class="btn btn-sm btn-info" style="float: right;margin-top:10px;margin-right: 10px;" onclick="window.close();">关闭窗口</button>
</div>
  </body>
  <div class="footer">
	<center>&nbsp;Copyright © 2016 总行信息科技部测试中心&nbsp;</center>
</div>
</html>
