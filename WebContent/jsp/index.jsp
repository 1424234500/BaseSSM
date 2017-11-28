<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  
<%@ include file="/include/title.jsp" %>  
 
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12"> 
		
<div class="panel panel-primary"  style="float:left;margin-right:10px;">
	<div class="panel-heading">
		Hello,world！
	</div>
	<div class="panel-body">
		 	<button class="btn btn-info" type="button" onclick="goUrl(sy.basePath+'/login/onlogin.do');">登录</button> 
			 <button class="btn btn-danger" type="button" onclick="goUrl(sy.basePath+'/student/listh.do');">学生信息</button>
			  <button class="btn btn-primary" type="button" onclick="goUrl(sy.basePath+'/file/list.do');">文件管理</button>
			  <button class="btn btn-warning" type="button">按钮</button>
			  <button class="btn btn-default" type="button">按钮</button>
	</div>
	<div class="panel-footer">Bootstrap 面板，使用基本的js jquery css + springmvc spring + hibernate mybatis 实现
		三层结构controller + service + dao mapper</div>
</div>
			
			
 
 

<div class="panel panel-info" style="float:left;margin-right:10px;">
    <div class="panel-heading">
        <h3 class="panel-title">AngularJs</h3>
    </div>
    <div class="panel-body">
		 	<button class="btn btn-info" type="button" onclick="goUrl(sy.basePath+'/jsp/angular/start.jsp');">test</button> 
		 	<button class="btn btn-info" type="button" onclick="goUrl(sy.basePath+'/jsp/angular/student.jsp');">测试列表 </button> 
		 	<button class="btn btn-info" type="button" onclick="goUrl(sy.basePath+'/jsp/angular/page.jsp');">学生信息</button> 
    </div>
    <div class="panel-footer">前端使用AngularJs实现单页面（仿C端）后台随意</div>
</div>
<div class="panel panel-warning" style="float:left;margin-right:10px;">
    <div class="panel-heading">
        <h3 class="panel-title">面板标题</h3>
    </div>
    <div class="panel-body">
        这是一个基本的面板
    </div>
</div>
<div class="panel panel-danger" style="float:left;margin-right:10px;">
    <div class="panel-heading">
        <h3 class="panel-title">面板标题</h3>
    </div>
    <div class="panel-body">
        这是一个基本的面板
    </div>
</div>



<div class="panel panel-success"  style="width:700px;float:left;margin-right:10px;">
    <div class="panel-heading">
        <h3 class="panel-title">图片轮播</h3>
    </div>
    <div class="panel-body">
        <div class="carousel slide" id="carousel-503731">
				<ol class="carousel-indicators">
					<li class="active" data-slide-to="0" data-target="#carousel-503731">
					</li>
					<li data-slide-to="1" data-target="#carousel-503731">
					</li>
					<li data-slide-to="2" data-target="#carousel-503731">
					</li>
				</ol>
				<div class="carousel-inner">
					<div class="item active">
						<img alt="" src="<%=base %>/include/img/hs.png" />
						<div class="carousel-caption">
							<h4> 浪花西口 </h4>
							<p> handshaker里的一幕，不知道具体的位置，看来却很有城市街头的狭小拥挤而冷清的感觉
							</p>
						</div>
					</div>
					<div class="item">
						<img alt="" src="<%=base %>/include/img/jj.png" />
						<div class="carousel-caption">
							<h4>城墙 </h4>
							<p>空旷，简单，大方，却是否有着不寻常的巨人在窥视着</p>
						</div>
					</div>
					<div class="item">
						<img alt="" src="<%=base %>/include/img/m52.png" />
						<div class="carousel-caption">
							<h4>路口 </h4>
							<p>恬静的路口，色彩鲜明，四月，是入学的季节，也是伤感的季节，你的眼里，是什么颜色，色彩斑斓，单调灰暗 </p>
						</div>
					</div>
				</div> <a data-slide="prev" href="#carousel-503731" class="left carousel-control">‹</a> <a data-slide="next" href="#carousel-503731" class="right carousel-control">›</a>
			</div>
    </div>
    <div class="panel-footer">
        success
    </div>
</div> 
 
		</div>
	</div>
</div> 
<%@ include file="/include/tail.jsp" %>   

<%@ include file="/include/foot.jsp" %> 
