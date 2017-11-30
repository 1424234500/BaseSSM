<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  


<script>
var urlList = sy.href;  

$(function(){   
	$("#list").click(function(){  
	    $("#myform").attr('action',urlList);   		
	    $("#myform").submit();    
	}) ; 
});  
function goAdd(){ 
	goUrl(sy.basePath + "/jsp/file/add.jsp");
}
function goUpdate(id){
	goUrl( sy.basePath + "/jsp/file/update.jsp?id=" + id  );
}
function goDetail(id){
	goUrl( sy.basePath + "/jsp/file/detail.jsp?id=" + id  );
}
function goDown(id){
	goUrl( sy.basePath + "/file/down.do?id=" + id  ); 
}
function ajaxDelete(id ){
	var url = sy.basePath + "/file/delete.do";   
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


</script> 

<%@ include file="/include/title.jsp" %>  
<%@ include file="/include/formhead.jsp" %>  

<div class="panel panel-info">

<div class="panel-heading"> 
	<h4>文件管理 </h4> 
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
	    <button id="list"   class="btn"  >查询</button>
</div>
	
	 <a class="btn" href="javascript:void(0)" onclick="goAdd();">添加</a>
<div class="panel-footer"> </div>


<div class="panel-body">
	<table class="table">
				<thead>
					<tr>
					
						<th> id </th>
						<th> name </th>
						<th> upuserid </th>
						<th> filesize</th>
						<th> type </th>
						<th> uptime </th>
						<th> changetime </th>
						<th> about </th>
						
						<th> down </th>
						<th> update </th>
						<th> delete </th>
					</tr>
				</thead>
				<tbody id="tableContent">
<c:forEach items="${res}" var="item">
			<tr> 
<!-- 文件管理 fileinfo: id,name,upuserid,filesize,type,path,uptime,changetime,about -->
			
			    <td class="info">${item.ID }</td>  
			    <td>
  			     <a class="btn" href="javascript:void(0)" onclick="goDown(${item.ID });"> ${item.NAME } </a> 
			    <td>${item.UPUSERID }</td>
			    <td>${item.FILESIZE }</td>
			    <td>${item.TYPE }</td>
			    <td>${item.UPTIME }</td>
			    <td>${item.CHANGETIME }</td>
			    <td>${item.ABOUT }</td>
			    <td>
			     <a class="btn" href="javascript:void(0)" onclick="goDetail(${item.ID });"> ${item.COUNT }</a> 
			    </td>
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
