<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  

<div  class="logintop"  >
<div class="divtopleft" > 

<img src="img/icon.png" />
CC后台管理
 
</div> 
<div class="divtopright"> 
	<img src="img/icon.png" style="width:0px;" /><!-- 同左边布局也添加那个图标以实现布局占用高度相同，让文本对齐一行,设置宽度0不显示 -->

	  管理员, ${sessionScope.id}  <a  class="buttontop" target="_top" href="<%=base %>/Manage?type=exit" >退出</a>
</div> 

  </div>
 
 
<%@ include file="/include/foot.jsp" %> 
