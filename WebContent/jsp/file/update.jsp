<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  

<%
	String id = request.getParameter("id") ; 
	String type = request.getParameter("type") ; 
%>

<script>
var id = '<%=id %>'; 
var type = '<%=type %>'; 

$(function(){  

	ajaxGet(id);
	
	
});  
//回退到主页
function goBack(){
	var url = sy.basePath + "/file/list.do";
	$(location).attr('href', url);
} 
function ajaxGet(id){
	var url = sy.basePath + "/file/get.do";   
	$.ajax({
        cache: true,
        type: "POST",
        url:url,
        data:{id: id},
        dataType: "json",
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(data) { 
        	//alert(data.obj.ID);
        	$("#id").attr('value',data.obj.ID);   	
        	$("#name").attr('value',data.obj.NAME);   	
        	$("#about").attr('value',data.obj.ABOUT);   	
        }
    });
	
}
function ajaxSubmit(id ){
	var url = "<%=basePath%>/file/update.do";   
	$.ajax({
        cache: true,
        type: "POST",
        url:url,
        data:$('#myform').serialize(),// 你的formid
        dataType: "json",
        async: false,
        error: function(request) {
            alert("Connection error");
        },
        success: function(data) { 
        	var res = parseInt(data);
        	if(res >= 1){
        		//alert("操作成功！更新" + res + "条");
				goBack();
        	}else{
        		alert("操作失败!");
        	} 
        }
    });
}
 
</script> 
 
<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
<div class="hero-unit"> <br>
<div class="panel panel-info">



<div class="panel-heading">
	更新
</div>

<div class="panel-body">
	<form id="myform" name="myform" action="<%=basePath%>/file/list" method="post">
		<fieldset>
		<label>Id</label> <br>
		<input id="id" name="id"  type="text" readonly="readonly" /> <br>
		<label>Name</label> <br>
		<input id="name" name="name"  type="text" readonly="readonly" /> <br>
		<label>About</label> <br>
		<input id="about" name="about"  type="text" /> <br>
	 	 <br>
		</fieldset>
	</form>
</div>
	
<div class="panel-footer">
	<button onclick="ajaxSubmit();"  class="btn">提交</button>
	<button onclick="goBack();"  class="btn">关闭</button>
</div>
	
</div></div></div></div></div> 

<%@ include file="/include/foot.jsp" %> 
