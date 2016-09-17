<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <base href="">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge"> 

  	<title>应用系统性能优化和安全堵漏实战</title>
  	
  	<link href="./js/bootstrap3/css/bootstrap.min.css" rel="stylesheet">
<script src="./js/jquery-1.11.1.min.js"></script>
<script src="./js/bootstrap3/js/html5shiv.min.js"></script>
<script src="./js/bootstrap3/js/respond.min.js"></script>
<script src="./js/bootstrap3/js/bootstrap.min.js"></script>

<script language="javascript">
 function gotopage(id){
   window.open("./QPerf"+id+".jsp");
 }
</script>

<style type="text/css">
body {
 
}

.header{
  position: relative;
  padding: 30px 15px;
  color: #cdbfe3;
  text-align: center;
  text-shadow: 0 1px 0 rgba(0,0,0,.1);
  background-color: #6f5499;
  background-image: -webkit-gradient(linear,left top,left bottom,from(#563d7c),to(#6f5499));
  background-image: -webkit-linear-gradient(top,#563d7c 0,#6f5499 100%);
  background-image: -o-linear-gradient(top,#563d7c 0,#6f5499 100%);
  background-image: linear-gradient(to bottom,#563d7c 0,#6f5499 100%);
  filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#563d7c', endColorstr='#6F5499', GradientType=0);
  background-repeat: repeat-x;
}
</style>
</head>
  
<body>
<div class="header">
 <h1>2016年总行第二直属工会“性能调优与安全堵漏”专项技能竞赛</h1>
</div>
<div class="container" style="">
    <div class="row" style="height:10px;"></div>
    <div class="row" style="">
        <div class="panel panel-default">
		  <div class="panel-heading"><b>参赛题目说明</b></div>
		  <div class="panel-body">
		  本次竞赛包含一道性能调优批量题目、一道性能调优联机题目和一道安全堵漏题目<br>
    <ul class="list-group" >
    <li class="list-group-item">(1)题目分值：性能调优批量题目50分、性能调优联机题目50分、安全堵漏题目100分，共200分。</br>
    <li class="list-group-item">(2)答题要求：需满足业务需求、功能可用、部署方式和启动方式需要满足投产场景需求。</li>
    </ul>
    	</div>
		</div>
	</div>
</div>


<div class="container" style="">
    <div class="row" style="height:10px;"></div>
    	<div class="row" style="">
	    <div class="col-md-12" style="padding-left:0px;">
	          <div class="panel panel-default">
			  <div class="panel-heading"><b>题目一、2016性能调优-批量题目</b></div>
			  <div class="panel-body">
			    <div class="row" style="padding-left:10px;">
			        <b>1.功能描述：</b>将本地文件中数据导入数据库并进一步按需求处理导入数据。</br>
			        <b>2.应用场景：</b>商品交易明细数据按月存在文件中，先需将2016年上半年6个月份的数据导入交易明细表（tab_trans_details），并以刚插入的数据为源数据，按每个商户每日所进行交易的收入总额、商户、日期信息进行抽取，抽取后数据存入商户日交易表（tab_store_trans_daily）。其中每个月数据对应一个文件，每个文件含28000条数据，如DetailsFile_201601.cvs代表2016年1月份数据文件。</br>
			        <b>3.答题要求：</b>在满足业务需求下，能够成功执行5次该批量处理程序且页面返回值每次成功导入16800条明细记录和8400条商户日交易记录，尽量缩短批量处理时间，要求无报错，无内存溢出。。</br>
			        <b>4.验证方式：</b>连续执行5次该批量，“批量总耗时”平均值作为处理耗时验证结果值，同时观察报错和审查代码优化情况。（注意:调优初始版本批量耗时在8分钟左右。）</br>
			      </div>
			    <div class="row" style="">
			    	<button type="button" class="btn btn-sm btn-info" style="float: right;margin-right: 10px;" onclick="gotopage('Batch');">功能页面</button>
			    </div>
			  </div>
			</div>
		</div>
	</div>
		<div class="row" style="">
	    <div class="col-md-12" style="padding-left:0px;">
	          <div class="panel panel-default">
			  <div class="panel-heading"><b>题目二、2016性能调优-联机题目</b></div>
			  <div class="panel-body">
			    <div class="row" style="padding-left:10px;">
			       <b>1.功能描述：</b>按需求进行数据库数据查询并返回结果列表到页面。</br>  
			       <b>2.应用场景：</b>将2015年上半年6个月对应在商户日交易表（tab_store_trans_daily）中的数据为源数据，查询获取位于浦东新区的商户中销售总额 前10名的商户信息（序号、商户ID、商户名称以及销售总额），并将结果返回到页面。</br>
			       <b>3.答题要求：</b>在满足业务需求、功能可用前提下，每个用户前次请求获取响应后停顿1秒再发起下一个请求的业务场景中需支持80在线用户持续稳定执行7分钟，要求无交易报错，无内存溢出，尽量缩短交易平均响应时间。</br>
			       <b>4.验证方式：</b>采用在线80用户迭代1秒同时执行查询，执行7分钟，取第1分钟到第6分钟间执行结果，计算平均响应时间作为验证结果值，同时观察报错和审查代码优化情况。</br>
			      </div>
			    <div class="row" style="">
			    	<button type="button" class="btn btn-sm btn-info" style="float: right;margin-right: 10px;" onclick="gotopage('Inquiry');">功能页面</button>
			    </div>
			  </div>
			</div>
		</div>
	</div>
	<div class="row" style="">
	    <div class="col-md-12" style="padding-left:0px;">
	          <div class="panel panel-default">
			  <div class="panel-heading"><b>题目三、2016安全堵漏题目</b></div>
			  <div class="panel-body">
			    <div class="row" style="padding-left:10px;">
			       <b>1.功能描述：</b>本系统为简单的用户管理系统，用户组包括普通用户和管理员。其中普通用户包括登录、个人信息查询修改、头像修改、密码修改，退出功能，管理员比普通用户多一个可以查看所有用户信息的功能菜单。</br>
                   <b>2.应用场景：</b>有一天，管理员（用户名admin密码123456）发现系统主页被黑、数据被拖库了。黑客留下简短的留言，声称这个网站存在SQL注入、跨站脚本攻击等OWASP TOP10 2013的漏洞，任何人不需要知道用户名和密码也可以登录网站，普通用户可以随意查其他用户的余额等敏感信息。另外黑客可通过上传webshell控制服务器，也可以通过远程代码执行漏洞控制服务器，甚至用浏览器输入一个恶意的URL就可以控制服务器并运行系统命令，真是太不安全了。请参赛选手帮助管理员提升系统的安全性。</br>
                   <b>3.答题要求：</b>安全堵漏部分的java代码在contest类中实现、jsp页面在web/contest目录下，请根据题目描述，阅读代码并修复尽可能多的安全缺陷。要求确保业务功能正确，勿修改用户的输入输出（如URL地址和参数、网页代码、系统提示信息等）和contest_user表的数据，否则会没有分数。</br>
                   <b>4.验证方式：</b>验证安全缺陷修复情况，根据缺陷修复数量和修复代码质量给出相应分数。</br> 
			      </div>
			    <div class="row" style="">
			    	<button type="button" class="btn btn-sm btn-info" style="float: right;margin-right: 10px;" onclick="gotopage('Safe');">功能页面</button>
			    </div>
			  </div>
			</div>
		</div>
	</div>
</div>

<div class="footer">
	<center>&nbsp;Copyright © 2016 总行信息科技部测试中心&nbsp;</center>
</div>

  </body>
</html>
