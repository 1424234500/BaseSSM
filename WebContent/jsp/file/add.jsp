<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  

<script> 

$(function(){    

});  
//回退到上级
function goBack(){
	var url = sy.basePath + "/file/list.do";  
 
	$(location).attr('href', url);
} 

function uploadFile(){
	  var url = sy.basePath + "/file/upload.do";  

	  var pic = $("#file").get(0).files[0];
	  var formData = new FormData();
	  formData.append("file" , pic); 
	  formData.append("about" , $("#about").val()); 
	  
	  $.ajax({
		   type: "POST",
		   url: url,
		   data: formData ,
		   processData : false, 
		   contentType : false , //必须false才会自动加上正确的Content-Type 
		   xhr: function(){
			    var xhr = $.ajaxSettings.xhr();
			    if(onprogress && xhr.upload) {
			  	    xhr.upload.addEventListener("progress" , onprogress, false);
			    	return xhr;
	   			}
	  		},
	  		error: function(request) {
	            alert("操作异常");
	        },
	        success: function(data) { 
	        	var res = parseInt(data);
	        	if(res >= 1){
	        		//alert("操作成功！");
					goBack();
	        	}else{
	        		alert("操作失败!");
	        	} 
	        }
	  });
}
// 侦查附件上传情况 ,这个方法大概0.05-0.1秒执行一次
function onprogress(evt){
	var loaded = evt.loaded;     //已经上传大小情况 
	var tot = evt.total;    	   //附件总大小 
	var per = Math.floor(100*loaded/tot);  //已经上传的百分比 
	$("#son").html( per +"%" ); 
	$("#son").css("width", per +"%" ); 

}
 
</script> 
 
 

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
<div class="hero-unit">  

  
<div class="panel panel-info" style="float:left;">


<div class="panel-heading">
 	添加  
</div>

<div class="panel-body">
			
	<form id="myform" name="myform"  method="post"  >  
		<fieldset> 
		        <input  class="btn" type="file" id="file" name="file" width="120px">  

			About<br>
			<textarea id="about" name="about" cols="50" rows="10" ></textarea> <br>
			
			
		</fieldset> 
		
	</form>
	<div id="son" style="background: #FF00FF;margin:0 auto;text-align:center;"></div> 
	
 </div>
 
<div class="panel-footer">

	<button onclick="uploadFile();"  class="btn">提交</button>
	<button onclick="goBack();"  class="btn">关闭</button>
</div>

</div></div></div></div></div>


<%@ include file="/include/foot.jsp" %> 
