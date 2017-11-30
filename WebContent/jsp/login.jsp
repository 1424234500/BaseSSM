<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  
 <%@ include file="/include/title.jsp" %>  
 

<script>


$(function(){    

});  

//回退到上级
function goBack(){
	var url = sy.basePath + "/";  
	goUrl(url);
} 
function ajaxSubmit(id ){
 	var url = sy.basePath + "/login/loginin.do";  
 	var loginUrl = sy.basePath + "/login/onlogin.do";  
	var afterLoginUrl = sy.basePath + "/";
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
				if(sy.href != loginUrl){	//若访问的不是登录页则为跳转过来的则跳回去，否则跳到默认页index
					afterLoginUrl = sy.href;
				}
				goUrl(afterLoginUrl);
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
<div class="hero-unit">  

  
<div class="panel panel-info" style="float:left;">



<div class="panel-heading">
 	登录
</div>

<div class="panel-body">
	<form id="myform" name="myform" method="post">
		<fieldset> 
			id<br>
			<input id="id" name="id"  type="text" /> <br>
			pwd<br>
			<input name="pwd"  id="pwd" type="text" />  
			 <br>
		</fieldset>
	</form>
 </div>
 
<div class="panel-footer">
	<button onclick="ajaxSubmit();"  class="btn">提交</button>
	<button onclick="goBack();"  class="btn">关闭</button>
</div>

 </div>
 
 
</div></div></div></div>


<%@ include file="/include/foot.jsp" %> 
