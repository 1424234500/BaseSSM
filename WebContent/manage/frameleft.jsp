<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  


 

<div class="divleft">
    <a  class="buttonleft" target="framemain" href="<%=base %>/Manage?type=listonline">在线用户</a>
       <a  class="buttonleft" target="framemain" href="<%=base %>/Manage?type=listlogin">登录记录</a>
    <a  class="buttonleft" target="framemain" href="<%=base %>/Manage?type=listuser">用户列表</a>
    <a class="buttonleft"  target="framemain" href="<%=base %>/Manage?type=listgroup">群组列表</a>
     <a  class="buttonleft" target="framemain" href="<%=base %>/Manage?type=listusermsg">用户消息记录</a>
    <a  class="buttonleft" target="framemain" href="<%=base %>/Manage?type=listgroupmsg">群组消息记录</a>
 <a   class="buttonleft"  target="_top" href="<%=base %>/Manage?type=exit" >退出</a>
</div>


<%@ include file="/include/foot.jsp" %> 
