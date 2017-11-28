 <%@page pageEncoding="UTF-8"%>   
  
 <!-- 导航栏-->

 
<!--  <nav class="navbar navbar-default" role="navigation"> -->
 <nav class="navbar navbar-inverse" role="navigation">
 
    <div class="container-fluid">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse"
                data-target="#example-navbar-collapse">
            <span class="sr-only">切换导航</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">主页</a>
    </div>
    <div class="collapse navbar-collapse" id="example-navbar-collapse">
        <ul class="nav navbar-nav">
            <li ><a href="javascript:void(0)"  onclick="goUrl(sy.basePath+'/student/listh.do');">信息管理</a></li>
            <li><a href="javascript:void(0)"  onclick="goUrl(sy.basePath+'/file/list.do');">文件管理</a></li>
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                   	 关于 <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <li class="active"><a href="#">bootstrap</a></li>
                    <li><a href="#">jquery</a></li>
                    <li><a href="#">easyui</a></li>
                    <li class="divider"></li>
                    <li><a href="#">springmvc</a></li>
                    <li class="divider"></li>
                    <li><a href="#">hibernate</a></li>
                    <li><a href="#">mybatis</a></li> 
                       <li class="divider"></li>
                    <li><a href="#">About</a></li>
                    <li><a href="#">Blog</a></li>
                </ul>
            </li>
        </ul>
    </div>
    </div>
</nav>