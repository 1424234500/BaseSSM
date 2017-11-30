<!-- <html>
    <head>
        <title>Line Chart</title>
        <script src="/BaseSSM/include/js/Chart.js"></script>
   
    </head>
    <body>  -->
    
<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  
    
<script>
var width = 400;
var height = 200;
$(function(){    	
	showLine( );

});  
function showLine( ){
	$("#myChartLine").attr('width',width);   		
	$("#myChartLine").attr('height',height);   	
	var ctx = document.getElementById("myChartLine").getContext('2d');

	var data =  {
	        labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange"],
	        datasets: [{
	            label: 'line',
	            data:  
	            	//[{ x: 10, y: 20  }, { x: 15, y: 10 }],
	            	[1, 2, 2, 8, 2, 3], 
	            backgroundColor: 'rgba(0, 0, 255, 0.5)',
	            borderColor: 'rgba(0, 255, 0)',
	            borderWidth: 1, 
	            showLine: true,
	        }]
	};
	var options = {
			showLines: true,
			spanGaps: true,
			scales: {
	            yAxes: [{  stacked: false  }] 
	        },
	        animation: {
	            duration: 1000, // general animation time
	        },
	        hover: {
	            animationDuration: 400, // duration of animations when hovering an item
	        },
	        responsiveAnimationDuration: 1000, // animation duration after a resize
	};
	var myChart = new Chart(ctx, {
	    type: 'line',
	    data: data,
	    options:options,
	});
}
//回退到上级
function goBack(){
	var url = sy.basePath + "/file/list.do";  
 
	$(location).attr('href', url);
} 
</script>    
    
   

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span12">
<div class="hero-unit">  

<div class="panel panel-primary" style="float:left;">
<div class="panel-heading">
 	下载上传统计  
</div>
<div class="panel-body">

	<div class="panel panel-info" style="float:left;">
		<div class="panel-heading">
		 	折线图 
		</div>
		<div class="panel-body">
			<canvas id="myChartLine" width="400" height="200"></canvas>
		</div>
	</div>
	
</div>
<div class="panel-footer">
	<button onclick="goBack();"  class="btn">关闭</button>
</div>

</div></div></div></div></div>


<%@ include file="/include/foot.jsp" %>  
