<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  


 <div class="divone">
 
<form id="queryform" name="queryform" action="<%=base %>/Manage?type=listgroup" method="post">

	ID:<input type="text"  style="width: 80px;" id="id" name="id" value="${requestScope.id}" /> 
 	昵称:<input type="text"  style="width: 80px;" id="username" name="username" value="${requestScope.username}" /> 
 	群主昵称:<input type="text"  style="width: 80px;" id="creatorusername" name="creatorusername" value="${requestScope.creatorusername}" /> 
 	签名:<input type="text"  style="width: 80px;" id="sign" name="sign" value="${requestScope.sign}" /> 

	<input type="submit" value="查询"   class="buttonblack" onclick=""/>  
	
	<!-- 显示查询结果列表 -->
	<table  class="table" id="table">
		<tr>
			<td > <div onclick="getElementById('ORDER').value='id'; getElementById('queryform').submit();">ID</div> </td>
			<td > <div onclick="getElementById('ORDER').value='username'; getElementById('queryform').submit();">昵称</div> </td>
			<td > <div onclick="getElementById('ORDER').value='creatorusername'; getElementById('queryform').submit();">群主昵称</div> </td>
			<td > <div onclick="getElementById('ORDER').value='profilepath'; getElementById('queryform').submit();">头像</div> </td>
			<td > <div onclick="getElementById('ORDER').value='sign'; getElementById('queryform').submit();">签名</div> </td>
			<td > <div onclick="getElementById('ORDER').value='num'; getElementById('queryform').submit();">最大人数</div> </td>
			<td > <div onclick="getElementById('ORDER').value='checked'; getElementById('queryform').submit();">无需验证</div> </td>


		</tr>
		
		<!-- 取出 名为 array的 对象 数组， 每一项名为 list引用 ，显示出来 ,分页提取显示-->
	<c:forEach items="${requestScope.objs}" var="list">
		<tr>
			<td>${list.ID}</td>
			<td>${list.USERNAME}</td>
			<td>${list.CREATORUSERNAME}</td>
			
			<td>${list.PROFILEPATH}</td>
			<td>${list.SIGN}</td>
			<td>${list.NUM}</td>
			<td>${list.CHECKED}</td>
			
		</tr>
	</c:forEach>  
	
	<%@ include file="/include/tablefoot.jsp" %> 
	

	</table>

</form>
</div> 


<%@ include file="/include/foot.jsp" %> 