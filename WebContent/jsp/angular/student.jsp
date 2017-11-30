<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  


<script> 
var urlGet = sy.basePath + "/angular/get.do";    
var urlList = sy.basePath + "/angular/list.do";    
 


$(function(){   
	info('$(function()){} execute')
	get("0002");
	info('$(function()){} end')

});   
function get(id){ 
	var url = urlGet;
	
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
        	//alert(data);
        	$("#id").attr('value',data.ID);   	
        	$("#name").attr('value',data.NAME);   	
        	$("#time").attr('value',data.TIME);   	
        }
    });
}
</script> 

<%@ include file="/include/title.jsp" %>  
<%@ include file="/include/cardhead.jsp" %>  



<div class="panel panel-info" ng-app="App"  ng-controller="listCtrl">

 	
<div class="panel-heading"> 
	<h4>学生信息管理 </h4> {{httpget}}
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
	    <button id="list"   class="btn" onclick="list()" >查询</button>
</div>
	
	 <a class="btn" href="javascript:void(0)" onclick="goAdd();">添加</a>
<div class="panel-footer"> </div>


<div class="panel-body">
	<table class="table">
				<thead>
					<tr>
						<th> No. </th>
						<th> id </th>
						<th> name </th>
						<th> time </th>
						<th> update </th>
						<th> delete </th>
					</tr>
				</thead>
				<tbody id="tableContent">
				
 
<tr ng-repeat='item in httplist'  ng-click='selectRowFun($index)' class='{menu-true: $index==selectedRow}' >   
			<td>{{$index + 1}}</td>   <!--  $index 在表格中显示出行号， -->
		    <td class="info">{{item.ID}}</td>  
		    <td class="warning">{{item.NAME }}</td>
		    <td class="error">{{item.TIME | date:'medium'}}</td>
		    <td> 
			     <a class="btn" href="javascript:void(0)" onclick="goUpdate(${item.ID });"> 修改</a> 
		    </td>
		    <td>
			     <a class="btn" href="javascript:void(0)" onclick="ajaxDelete(${item.ID });"> 删除</a> 
		    </td>
</tr>  
				</tbody>
				
				<%@ include file="/include/tablefoot.jsp" %>  

	</table>  
	{{httplist}}
</div>

</div> 
<%@ include file="/include/cardfoot.jsp" %>   
<%@ include file="/include/foot.jsp" %>  

<script type="text/javascript">
var app = angular.module('App', ['ngRoute']); 


app.factory('service', function($http,$resource) {	
	var res = {};  	//新建结果返回的类
	res.list = $resource('/BaseSSM/angular/list.do',{id: '@'},
	{ 
		sendEmail: {
			method: 'PUT',
			transformRequest: function(data, headerFn) {
				// 返回修改后的请求数据
				return  data;
			}
		}
	});
	
	return res;
});

app.controller('listCtrl', function($scope, $rootScope, $http) { 
	info("listCtrl begin");	 
	$http({
		method: 'GET',
		url: '/BaseSSM/angular/get.do',
		params: {
			'id': '0002'
		},
	})
	.then(
		function successCallback(response) {
			$scope.httpget = response.data;
		}, 
		function errorCallback(response) {
			$scope.httpget = "errorCallback: " + response.	hbdata;
		}
	);
	$http({
		method: 'GET',
		url: '/BaseSSM/angular/list.do',
		params: {
			'id': '0002'
		},
	})
	.then(
		function successCallback(response) {
			$scope.httplist = response.data;
		}, 
		function errorCallback(response) {
			$scope.httplist = "errorCallback: " + response.data;
		}
	);
	
	info("listCtrl end");
});
</script> 
