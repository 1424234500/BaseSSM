<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  
 
<%@ include file="/include/title.jsp" %>   

 <!-- 页面page 主窗口容器 -->
<%@ include file="/include/cardhead.jsp" %>  
 
<div ng-app="App"  ng-controller="pageCtrl">
	<div class="panel panel-info"  ng-view>
	<!-- ///////////////////////////////////////////// -->
	</div>
</div>  
<%@ include file="/include/cardfoot.jsp" %>  
<%@ include file="/include/tail.jsp" %>   
<%@ include file="/include/foot.jsp" %>  

<script type="text/javascript">
var app = angular.module('App', ['ngRoute']); 
app.config(['$routeProvider', function($routeProvider) {
	// 在这里定义路由
	$routeProvider.  
	when('/', {
		controller: 'listCtrl',
		templateUrl: '/BaseSSM/jsp/angular/list.jsp'  
	}).  // 注意，为了创建详情视图，我们在 id 前面加了一个冒号，从而指定了一个参数化的 URL 组件  
	when('/add', {
		controller: 'addCtrl',
		templateUrl: '/BaseSSM/jsp/angular/add.jsp'  
	}).
	when('/update/:id', {
		controller: 'updateCtrl',    
		templateUrl: '/BaseSSM/jsp/angular/update.jsp'  
	}).  
	otherwise({
		redirectTo: '/'  
	}); 
}]);
 

app.controller('listCtrl', function ($scope,$rootScope, $route, serviceTest, providerTest, factoryTest) { 
	info("listCtrl begin");	 
	info("test execute service,provider,factory");
	info(serviceTest);
	info(providerTest);
	info(factoryTest); 
	
	//bootstrap日期插件使用方式
	
	$('#timefrom').datetimepicker();
	$('#timeto').datetimepicker();

	serviceTest.testPromiseQ().then(
		function(res){
			$scope.testPromiseQ = "$q/promise: res=" + res;
		},
		function(err){
			$scope.testPromiseQ = "$q/promise: err=" + err;
		}
	);
	
	$scope.search = {};	//查询
    $scope.orderType = 'id';
    $scope.order = '-';
	$scope.changeOrder = function(type){
        $scope.orderType = type;
        if($scope.order === ''){
            $scope.order = '-';
        }else{
            $scope.order = '';
        }
    };
	$scope.list = function(){ 
		//debugger;
		var PAGE = $scope.PAGE;
		var search = $scope.search;
		info("PAGE, search:");
		info(PAGE);
		info(search);
		params = $.extend({}, PAGE, search);
		info(params);
		serviceTest.list(params).then(
			function successCallback(response) {
				$scope.httplist = response.data.res;
				$scope.PAGE = response.data.PAGE;
			} 
		);
	};
	$scope.list();

	$scope.ajaxDelete = function(id){ 
		var params = {"ID":id};
		serviceTest.url("/BaseSSM/angular/delete.do", params).then(
			function successCallback(response) {
				info("修改数据:" + response.data.res + "条");
				$scope.list();		
			}, 
			function errorCallback(response) {
				info("修改数据:" + response.data.res + "条");
				alert("修改数据:" + response.data.res + "条");
			}
		);
	}
	
	
	info("listCtrl end");	 
});


app.controller('addCtrl', function ($scope, $http, $routeParams, serviceTest) { 
	info("addCtrl begin");	 
	
	$('#time').datetimepicker();

	$scope.gopage = function(){
		goUrl('BaseSSM/jsp/angular/page.jsp#/');
	};
	$("#gopage").click(function(){
		$scope.gopage();
	});
	
	$scope.params = $routeParams;
	info("routeParams");
	info($scope.params);
	 
	$scope.ajaxUpdate = function(){
		//debugger;
		info("ajaxUpdate now httpget=");
		info($scope.httpget);

		serviceTest.url("/BaseSSM/angular/update.do", $scope.httpget).then(
			function successCallback(response) {
				info("修改数据:" + response.data.res + "条");
				$scope.gopage();
			}, 
			function errorCallback(response) {
				info("修改数据:" + response.data.res + "条");
				alert("修改数据:" + response.data.res + "条");
			}
		);
	}
	
	info("addCtrl end");	 
});
app.controller('updateCtrl', function ($scope, $http, $routeParams, serviceTest) { 
	info("updateCtrl begin");	
	
	$('#time').datetimepicker();

	$scope.gopage = function(){
		goUrl('BaseSSM/jsp/angular/page.jsp#/');
	};
	$("#gopage").click(function(){
		$scope.gopage();
	});
	
	$scope.params = $routeParams;
	info("routeParams");
	info($scope.params);
	//$scope.httpget = {ID:'', NAME:'', TIME:''};
	serviceTest.get($scope.params.id).then(
		function successCallback(response) {
			$scope.httpget = response.data;
		}, 
		function errorCallback(response) {
			$scope.httpget = "!!!" + response.	hbdata;
		}
	);
	$scope.ajaxUpdate = function(){
		//debugger;
		info("ajaxUpdate now httpget=");
		info($scope.httpget);

		serviceTest.url("/BaseSSM/angular/update.do", $scope.httpget).then(
			function successCallback(response) {
				info("修改数据:" + response.data.res + "条");
				$scope.gopage();
			}, 
			function errorCallback(response) {
				info("修改数据:" + response.data.res + "条");
				alert("修改数据:" + response.data.res + "条");
			}
		);
	}
	
	info("updateCtrl end");	 
});
app.controller('pageCtrl', function($scope, $http) { 
	info("pageCtrl begin");	 
	
	
	info("pageCtrl end");
});

 
// 自定义服务 provider不能注入 $http？
app.provider('providerTest',function(){
    this.$get = function(){
        return {
            message : 'providerTest',
            list : function(){
            	return 'list function';
            } 
        }
    }
});
// 自定义工厂
app.factory('factoryTest',function($http){
	var factory = {};
	factory.name = 'factoryTest.name ';
	factory.list = function(){
		return 'factoryTest.list ';
	}
    return factory;
});
// 自定义服务
app.service('serviceTest',function($http, $q){
	var service = {};
	service.name = 'serviceTest.name ';
	service.url = function(url, params){
		info('service.url,params:');
		info(url);
		info(params);
		//params = {id:params.ID}; 
		return $http({ 
			method: 'GET',
			url: url,
			params: params, 
		}) ;
	};
	
	service.testPromiseQ = function () {
          var defered = $q.defer();
          $http.get("/BaseSSM/angular/list.do")
              .success(function (data) {
                  defered.resolve(data.res);
              })
              .error(function (err) {
                  defered.reject(err);
              });
          return defered.promise; // 把defered对象中的promise对象返回出来
      };
      
	service.list = function(params){
		return $http({
			method: 'GET',
			url: '/BaseSSM/angular/list.do',
			params: params,
		}) ;
	};
	service.get = function(id){
		return $http({
			method: 'GET',
			url: '/BaseSSM/angular/get.do',
			params: {
				'id': id
			},
		}) ;
	};
    return service;
});

//diy过滤器  结合 ng-repeat实现本页面静态查询
app.filter('filterQuery', function() { 
	return function (collection, params) {
        var res = [];
        info("collection：");
        info(collection);
        info("params:");
        info(params); 
        
        angular.forEach(collection, function (item) {
        	info(item);
            flag = 1;
			//debugger;
            //过滤数组中值与指定值相同的元素 
            /* if(item.TIME != null){ 
            	flag = 0;
            	if(params.TIMETO != null && params.TIMEFROM != null && item.TIME < parmas.TIMETO && item.TIME >= params.TIMEFROM){
            		flag = 1;
            	}else if(params.TIMETO != null && params.TIMEFROM != null && item.TIME < params.TIMETO ){
            		flag = 1;
            	}else if(params.TIMETO == null && params.TIMEFROM != null && item.TIME >= params.TIMEFROM ){
            		flag = 1;
            	}else if(params.TIMEFROM == null && params.TIMETO == null ){
            		flag = 1;
            	}
            } */
            if(flag == 1){
            	res.push(item);
            }
        });
        return res;
    }
});

 

</script>
  
