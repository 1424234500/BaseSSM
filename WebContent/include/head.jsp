<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 

 
 <!-- 页面常用jsp参数 -->
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();%>
<%String contextPath = request.getContextPath();%>
<%String base = contextPath;//项目名，统一所有需要用到项目名前缀，方便统一修改 /BaseSSM %>
<%String version = "2017-09-05 14:46:25";%>

<%
Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
Cookie[] cookies = request.getCookies();
if (null != cookies) {
	for (Cookie cookie : cookies) {
		cookieMap.put(cookie.getName(), cookie);
	}
}
String easyuiTheme = "metro-blue";//指定如果用户未选择样式，那么初始化一个默认样式 
%>

  
<html>
<head>
<base href="<%=basePath%>">

<!-- 日期选择器wdate picker -->
<script src="<%=base %>/include/datepicker/WdatePicker.js" type="text/javascript" charset="utf-8"></script>

<!-- jQuery文件 务必在bootstrap.min.js 之前引入 -->
<script src="<%=base %>/include/js/jquery-3.2.1.js" type="text/javascript" charset="utf-8"></script>


<!-- Bootstrap -->
<link href="<%=base %>/include/bootstrap/css/bootstrap.css" rel="stylesheet">
<script src="<%=base %>/include/bootstrap/js/bootstrap.js" type="text/javascript" charset="utf-8"></script>
<!-- Bootstrap日期选择插件 -->
<link href="<%=base %>/include/bootstrap/css/bootstrap-datetimepicker.css" rel="stylesheet">
<script type="text/javascript" src="<%=base %>/include/bootstrap/js/bootstrap-datetimepicker.js" charset="UTF-8"></script>

<!-- 图表插件Chart.js -->
<script src="<%=base %>/include/js/Chart.js"  type="text/javascript" charset="utf-8"></script>
<!-- AngularJs框架 -->
<script src="<%=base %>/include/js/angular.js"  type="text/javascript" charset="utf-8"></script>
<!-- AngularJs路由插件 单页面 -->
<script src="<%=base %>/include/js/angular-route.js"  type="text/javascript" charset="utf-8"></script>

<!-- <script src="http://cdn.static.runoob.com/libs/angular.js/1.4.6/angular.min.js"></script>-->






<!-- 页面常用js变量和函数工具 -->
<script type="text/javascript"> 
	var localhost="http://localhost:8080" + '<%=contextPath %>';
	var sy = sy || {};
	sy.href = location.href;
	sy.contextPath = '<%=contextPath%>';
	sy.basePath = '<%=basePath%>';
	sy.version = '<%=version%>';
	sy.pixel_0 = '<%=contextPath%>/style/images/pixel_0.gif';//0像素的背景，一般用于占位
	
	
	
	//跳转到url
	function goUrl(url){
		$(location).attr('href', url);
	}
	function info(str){
		console.info(str);
	}
	function out(str){
		info(str);
	}
	
	
</script>


</head>



<body>



 
 
 
 
 