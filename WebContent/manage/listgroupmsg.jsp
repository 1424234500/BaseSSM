<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  


 <div class="divone">
 
<form id="queryform" name="queryform" action="<%=base %>/Manage?type=listgroupmsg" method="post">

	发送人ID:<input type="text"  style="width: 80px;" id="fromid" name="fromid" value="${requestScope.fromid}" /> 
 	群ID:<input type="text"  style="width: 80px;" id="groupid" name="groupid" value="${requestScope.groupid}" /> 
 	发送人昵称:<input type="text"  style="width: 80px;" id="fromusername" name="fromusername" value="${requestScope.fromusername}" /> 
 	群名称:<input type="text"  style="width: 80px;" id="toname" name="toname" value="${requestScope.toname}" /> 
	发送时间:<input  type="text" id="time" name="time" value="${requestScope.time}" style="width: 70px;" onFocus="WdatePicker({readOnly:false,dateFmt:'yyyy-MM-dd'})"  /> 
 	消息 :<input type="text"  style="width: 80px;" id="msg" name="msg" value="${requestScope.msg}" /> 
	
	<input type="submit" value="查询"   class="buttonblack" onclick=""/>  
	
	<!-- 显示查询结果列表 -->
	<table  class="table" id="table">
		<tr>
			<td > <div onclick="getElementById('ORDER').value='fromid'; getElementById('queryform').submit();">发送人ID</div> </td>
			<td > <div onclick="getElementById('ORDER').value='groupid'; getElementById('queryform').submit();">接收人ID</div> </td>
			<td > <div onclick="getElementById('ORDER').value='fromusername'; getElementById('queryform').submit();">发送人昵称</div> </td>
			<td > <div onclick="getElementById('ORDER').value='toname'; getElementById('queryform').submit();">群名称</div> </td>
			<td > <div onclick="getElementById('ORDER').value='time'; getElementById('queryform').submit();">时间</div> </td>
			<td > <div onclick="getElementById('ORDER').value='msg'; getElementById('queryform').submit();">消息</div> </td>
			<td > <div onclick="getElementById('ORDER').value='type'; getElementById('queryform').submit();">消息类型</div> </td>


		</tr>
		
		<!-- 取出 名为 array的 对象 数组， 每一项名为 list引用 ，显示出来 ,分页提取显示-->
	<c:forEach items="${requestScope.objs}" var="list">
		<tr>
			<td>${list.FROMID}</td>
			<td>${list.GROUPID}</td>
			<td>${list.FROMUSERNAME}</td>
			<td>${list.TONAME}</td>
			<td>${list.TIME}</td>
			<td>${list.MSG}</td>
			<td>${list.TYPE}</td>
			
		</tr>
	</c:forEach>  
	
	<%@ include file="/include/tablefoot.jsp" %> 
	

	</table>

</form>
</div> 


<%@ include file="/include/foot.jsp" %> 