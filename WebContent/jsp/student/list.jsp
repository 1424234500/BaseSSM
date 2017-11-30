<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  


<script> 
var urlList = sy.basePath + "/student/list.do";    

$(function(){    

	var urlList = sy.href;  

	$(function(){   
		$("#list").click(function(){  
		    $("#myform").attr('action',urlList);   		
		    $("#myform").submit();    
		}) ; 
	});  
	function goAdd(){ 
		goUrl(sy.basePath + "/jsp/student/add.jsp");
	}
	function goUpdate(id){
		goUrl( sy.basePath + "/jsp/student/update.jsp?id=" + id  );
	}
	function goDetail(id){
		goUrl( sy.basePath + "/jsp/student/detail.jsp?id=" + id  );
	} 
	function ajaxDelete(id ){
		var url = sy.basePath + "/student/delete.do";   
		$.ajax({
	        cache: true,
	        type: "POST",
	        url:url,
	        //data:$('#myform').serialize(),// 你的formid
	        data:{ id:id},
	        dataType: "json",
	        async: false,
	        error: function(request) {
	            alert("Connection error");
	        },
	        success: function(data) { 
	        	var res = parseInt(data);
	        	if(res >= 1){
	        		//alert("操作成功！删除" + res + "条");
	     			$("#myform").attr('action',urlList);   		
	        		$("#myform").submit();   
	        	}else{
	        		alert("操作失败!");
	        	} 
	        }
	    });
	}

	
});    
</script> 

<%@ include file="/include/title.jsp" %>  

<%@ include file="/include/formhead.jsp" %>  



<div class="panel panel-info">

 	
<div class="panel-heading"> 
	<h4>学生信息管理 </h4> 
</div>
<div class="panel-footer">
		查询条件
		id
		<input name="id" id="id" value="${id}" class="input-medium search-query" type="text" />  
		name
		<input name="name"  id="name" value="${name}" class="input-medium search-query" type="text" />  
		timefrom
		<input  name="timefrom" value="${timefrom}" id="timefrom" onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="input-medium search-query" type="text" />  
		timeto
		<input name="timeto"  id="timeto" value="${timeto}"  onFocus="WdatePicker({readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="input-medium search-query" type="text" />  
	    <button id="listh"   class="btn"  >hbnat查询</button>
	    <button id="listm"   class="btn"  >mbat查询</button> 
</div>
	
	 <a class="btn" href="javascript:void(0)" onclick="goAdd();">添加</a>
<div class="panel-footer"> </div>


<div class="panel-body">
	<table class="table">
				<thead>
					<tr>
						<th> id </th>
						<th> name </th>
						<th> time </th>
						<th> update </th>
						<th> delete </th>
					</tr>
				</thead>
				<tbody id="tableContent">
<c:forEach items="${res}" var="item">
			<tr> 
			    <td class="info">${item.ID }</td>  
			    <td class="warning">${item.NAME }</td>
			    <td class="error">${item.TIME }</td>
			    <td>
			     <a class="btn" href="javascript:void(0)" onclick="goUpdate(${item.ID });"> 修改</a> 
			    </td>
			    <td>
			     <a class="btn" href="javascript:void(0)" onclick="ajaxDelete(${item.ID });"> 删除</a> 
			    </td>
			</tr>
</c:forEach>
				</tbody>
				
				<%@ include file="/include/tablefoot.jsp" %>  

	</table>  
</div>

</div>

<%@ include file="/include/formfoot.jsp" %>  

 
 
<%@ include file="/include/foot.jsp" %> 
