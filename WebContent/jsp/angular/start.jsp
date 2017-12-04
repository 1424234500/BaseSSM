<%@page pageEncoding="UTF-8"%>   
<%@ include file="/include/head.jsp" %>  
 
<style>
	input.ng-invalid {
	    background-color: lightblue;
	} 
	.menu-true {
		color: blue;
		background-color: red;
	}
	.menu-false {
		color: red;
		background-color: blue;
	}
	img{ 
		height:5%;
		overflow:hidden;
	}
	 
</style> 
<div  ng-app="App"  class="panel panel-info">
<!--  	
	 ng-bind=={{}}是从$scope -> view的单向绑定
	 ng-modle是$scope <-> view的双向绑定 
-->
<script type="text/javascript">
var app = angular.module('App', []); //定义加【】 引用不加

//mvc的分离， control只负责链接 service函数 和 数据模型 不处理数据  再多嵌套一层函数？ <- 本来直接绑定标签事件到数据处理函数，现在中间层+1control，集中管理
//watch监控数据并修改 || 过滤器修改数据 || 函数处理数据

</script> 

<!--  表单
ng-minlength="5" ng-pattern="[a-zA-Z]" type="email type="number" type="url" -->

<div class="panel-footer" ng-clock><!-- ng-clock 无效？ -->
 名字: <input type="text" ng-model="name" name="nameName" required> <span ng-click="name=name+1">ng-click="name=name+1"</span>  <br>
Hello rootScope.name:{{name + " -- "}}  name+5={{ name + 5 }} name*5={{ name * 5 + "--" }}5/name={{ 5/name + "--" }}<br> 
绑定函数返回值 :{{bindFun()}}<br> 
折后价root :{{discount}}<br>
<span ng-show="name < 10">ng-show{{name}}</span>
<span ng-hide="name < 2">ng-hide{{name}}</span>
<span ng-if="name < 20">ng-if{{name}}</span>
<div ng-switch on="name">
	<p ng-switch-default>ng-switch-default on='name' </p>
	<h4 ng-switch-when="8">{{  "ng-switch-when='8'" }}</h1>
</div>
</div>

<div  ng-controller="myCtrl" class="panel-heading"> 
<div >折后价 :{{discount}}</div>
timeout:{{clock}}--{{clock |  date:'medium' }} <br>

名: <input type="text" ng-model="firstName"><br>
姓: <input type="text" ng-model="lastName"><br>
<br>
姓名: <div ng-model="fullName" >{{firstName + " " + lastName}}</div>
<div>{{mapName}}</div>
	<div style="float:left;" ng-repeat="name in mapName | orderBy:'country'">
		{{"-" +( name.name  | uppercase) +"." + name.country}}
	</div> 
	<br>
</div>
<script type="text/javascript">
//运行块通常用来注册全局的事件监听器。例如，我们会在.run()块中设置路由事件的监听器
//以及过滤未经授权的请求
app.run(function($rootScope){	//只能注入 rootSocpe 
	console.info("app.run main init")
	
});
//显示注入方式
//app.controller('myCtrl',[ '$scope', '$q',function ($scope, $q) {} ]);
app.controller('myCtrl', function($scope,$rootScope,$timeout, $interval){
	//timeout单次延时任务 interval周期定时任务
     var updateClock = function( ) {
          $scope.clock = new Date();
          $timeout(function() {
            updateClock();
          }, 1000);   
      };   
      updateClock(); 

      $interval(function () {
          info("$interval 周期定时任务10s");
      }, 10000);
      
      
	out("scope.name:"+$scope.name);
	out("rootScope.name:"+$rootScope.name); //从内到外 变量寻址 局部函数访问域类似

	$rootScope.bindFun = function(){
		return $rootScope.name * 10;
	};
 	//监控数据watch true/false 对象/引用监听？ 'name'
 	$rootScope.$watch($rootScope.bindFun, function(newValue, oldValue, scope){//最后参数scope无效？都是用的myCtrl访问域里的两个scope/root
 	    //$watch函数时把 items写成了一个字符串。这样做是可以 的，因为 $watch 函数既可以接受一个函数（就像我们前面做的），也可以接受一个字符串。 如果把一个字符串传递给了 $watch 函数，它将会在被调用的 $scope 作用域中当成表达 式来执行。
 	    $scope.discount = newValue > 100 ? newValue/2 : "没有折扣";  
 	    $rootScope.discount = newValue > 100 ? newValue/10 : "没有折扣";  
 	});  

    $scope.firstName= "John";
    $scope.lastName= "Doe";
    $scope.fullName = function() {
        return $scope.firstName + "-ngmodel-" + $scope.lastName;
    }
    $scope.mapName = [
        {name:'Jani',country:'Norway'},
        {name:'Hege',country:'Sweden'},
        {name:'Kai',country:'Denmark'}
    ];
});
 
</script>

<!-- currency	格式化数字为货币格式。<br>
filter	从数组项中选择一个子集。<br>
lowercase	格式化字符串为小写。<br> 
orderBy	根据某个表达式排列数组。<br>
uppercase	格式化字符串为大写。<br> -->
 
<div ng-controller="formCtrl"  ng-init="num=1;price=5;person={firstName:'John',lastName:'Doe'};points=[1,15,19,2,40]">
<p>总价： <span ng-bind="price * num"></span></p>
<p>对象：{{person.firstName + "-" + person.lastName + "-" + points[1]}}</p>
</div>   

<form name="myForm" ng-submit="submit()" action="/BaseSSM/angular/get.do">
	Name:
	<input type="text" name="name" ng-model="formname"  ng-change="change()" required/> <br>
    Email:
    <input type="email" name="myAddress" ng-model="text"  ng-maxlength='12' ng-minlength='3' ngbk-focus >	<!-- 焦点 -->
    <span style="color:red;" ng-show="myForm.myAddress.$error.email">不是一个合法的邮箱地址</span>
     状态 
    {{"myForm.valid="+myForm.$valid}}
    {{myForm.myAddress.$dirty}}
    {{myForm.myAddress.$touched}}
    <br>
    <input type="checkbox" ng-model="isTwoFish">[{id=id-001, key=key-001, username=username-001}]<br/> 
    <select>   
    
	    <option ng-repeat='item in httplist' value="{{item.id}}">
			{{"item[i].username=" + item.username  }}
		</option>
	    <option>One Fish</option>   
	    <option ng-selected="isTwoFish">Two Fish</option> 
    </select> 
    <input type="submit" class="button-info" title="click me to submit" ng-disabled='!myForm.$valid' />
 </form>
<script type="text/javascript">
app.controller('formCtrl', function($scope){
	console.info("formCtrl init");
	$scope.submit = function(){
		console.dubug("formCtrl . ng-submit ()")
	};
	$scope.change = function(){
		console.info("change:" + $scope.formname); 
	};

});

</script>
 
<!-- 
ng-valid: 验证通过
ng-invalid: 验证失败
ng-valid-[key]: 由$setValidity添加的所有验证通过的值
ng-invalid-[key]: 由$setValidity添加的所有验证失败的值
ng-pristine: 控件为初始状态
ng-dirty: 控件输入值已变更
ng-touched: 控件已失去焦点
ng-untouched: 控件未失去焦点
ng-pending: 任何为满足$asyncValidators的情况 --> 

<div class="panel-heading">
	测试自定义模板
</div>
<div class="panel-body">
<input type="text" ng-model="myUrl"/>
<input type="text" ng-model="myText"/>  ->
<!-- <self-directive></self-directive>  -->
</div> 
<script type="text/javascript">
//自定义模板   自定义指令
//selfDirective  -    self-directice
app.directive("selfDirective", function() {
    return {
    	replace : true,	//编译之后的html是否替换模板代码
     	restrict : "EAC",
     	/* E 作为元素名使用 A 作为属性使用 C 作为类名使用 M 作为注释使用  默认值为 EA, 即可以通过元素名和属性名来调用指令。 */
        scope: { //{}隔离区
        	myUrl: '=myUrl',
        	myText: '=myText'
        	
        	//@常量 =双向绑定 &函数传递？
        },
        template : '<a href="{{myUrl}}">{{myText}}</a>  <span ng-transclude></span>  '  ,
        transclude : true
        
        //compile
        //link  事件
    };
}); 
</script>




<div class="panel panel-info" ng-controller='tableController'>
aValue in father: {{model.aValue}} <button ng-click="fatherClick()">设置father的aValue值</button>
<div class="panel-footer"  ng-controller='showController'>
aValue in child: {{model.aValue}}<button ng-click="childClick()">设置child的aValue值</button><br>
	<button ng-click='buttonClick()'  ng-disabled='isDisablee'>点击控制显示属性  show显示属性绑定  class名绑定 所以任何html中的东西都可以绑定？用以动态生成html的脚本？EL表达式？</button>
	<ul ng-show='menuState'>    
		<li ng-click='stun()'  class='menu-{{isCssDisabled}}'>css class名绑定</li>    

		<li><a href="{{hrefIn}}">href in 5 seconds.</a></li>  
		<li><a ng-href="{{hrefIn}}">ng-href in 5 seconds.</a></li>  
	
		<li ng-click='disintegrate()'   > img src in 5 seconds.<img src="{{imgIn}}"/></li>    
		<li ng-click='erase()'> img ng-src in 5 seconds. <img src="{{imgIn}}"/></li>  
		<li >  {{factoryTest}}</li>  
		<li >  {{factoryTest2}} </li>  
		<li >  {{factoryTest3}} </li>  
	</ul>  
</div>
<script type="text/javascript">

app.controller('showController', function($scope, $http, $timeout, Items) { 
	Items.setName("setNameWalker");
	$scope.factoryTest = "Items.test="+Items.test + "----Items.getName()=" + Items.getName() + "----Items.getName=" + Items.getName+"----Items.Name=" + Items.Name+"----Items.name/this.name=" + Items.name ;
	
	
	
	//同步调用，获得承诺接口  
    Items.query().then(function(data) {  // 调用承诺API获取数据 .resolve  
        $scope.factoryTest2 ="Items.query=" + data;  
    }, function(data) {  // 处理错误 .reject  
        $scope.factoryTest2 ="Items.query=" + data;  
    });   
	
	$scope.model = {         aValue: 'child 初始化设置aValue'     } 
	$scope.childClick = function(){
	 	$scope.model .aValue = "child Click 设置aValue 只改变child的？"
	}; 

	$timeout(function() {         
		$scope.hrefIn = 'http://google.com';    
		$scope.imgIn = '/BaseSSM/include/img/jj.png'; 
	}, 5000); 
	
	
	$scope.isDisablee = false;     
	
	timeAble = function(){
		$timeout(time = function(){
			$scope.isDisablee = !$scope.isDisablee;     
			out("timeAble");
			timeAble();
		}, 2000);
	}
	timeAble();
	
	$scope.menuState = true;
  	$scope.buttonClick = function() {    
	  	$scope.menuState = !$scope.menuState;  
  	};
	$scope.isCssDisabled = true;
 	$scope.stun = function() {    
	  	$scope.isCssDisabled = !$scope.isCssDisabled;  
  	};
  /* 	$scope.disableTrue = true;
  	$scope.disableFalse = false;
 	$scope.disintegrate() = function() {    
	  	$scope.disableTrue = false;
	  	$scope.disableFalse = true;  	
  	}; */
});
  
</script>

<div class="panel-heading"> 
	<h4>AngularJs测试 </h4> 
</div>
<div class="panel-footer">
	 httpget:{{httpget}} 
</div> 
<div class="panel-body">
	<span ng-repeat='item in httpget'>
		{{(" item[i]=" + item ) | titleCase}}
	</span>
</div>


<div class="panel-footer"> 
	{{( httplist)}} 
</div>   
<div class="panel-body" >
	<table class="table">
				<thead>
					<tr>
						<th> No. </th>
						<th> id </th>
						<th> key </th>
						<th> username </th>
						<th> control </th>
						<th> img </th>
					</tr>
				</thead>
				<tbody id="tableContent">
<!-- row 选中事件 并 ng-class控制选中色  判断条件true才会绑定class？ -->
<tr ng-repeat='item in httplist'  ng-click='selectRowFun($index)' 
class='{menu-true: $index==selectedRow}' >   
<!-- class='menu-{{colorClick}}' -->
		<td>{{$index + 1}}</td>   <!--  $index 在表格中显示出行号， -->
		<td>{{item.ID}}</td>    
		<td>{{item.NAME}}</td>  
		<td>{{item.TIME}}</td>  
	    <td>  <a class="btn" href="javascript:void(0)" onclick="ajaxDelete(${item.ID });"> 删除</a>  </td>
	    <td>  <img ng-src="/BaseSSM/include/img/{{imgsrc}}" />  </td>
</tr> 
				</tbody>
				
<%@ include file="/include/tablefoot.jsp" %>  
 
</table>
</div>
<script type="text/javascript">
app.controller('tableController', function($scope, $rootScope, $http) { 
	$scope.model = {         aValue: 'hello father 初始化设置aValue'     } 
	$scope.fatherClick = function(){
		 $scope.model .aValue = "father Click 设置aValue 会改变 father的和child的？"
	};

	$scope.imgsrc = "hs.png"; 
	$scope.selectRowFun = function(row) {
	
		out("点击了" + row); 
		$scope.selectedRow = row; 
		//$scope.colorClick = true; 
	}; 

	$http({
		method: 'GET',
		url: '/BaseSSM/angular/get.do'
	})
	.then(
		function successCallback(response) {
			$scope.httpget = response.data;
			$rootScope.httpget = response.data;	//把私有ctrl变量放到全局去，然后其他ctrl就能访问到全局的，共享数据的一种方式
			//data= [{"id":"id0","key":"key-0","username":"username-0"},{"id":"id1","key":"key-1","username":"username-1"}]
		}, 
		function errorCallback(response) {
			$scope.httpget = "errorCallback: " + response.data;
		}
	);
	
	$http({
		method: 'GET',
		url: '/BaseSSM/angular/list.do',
		params: {
			'username': 'auser'
		}
	}).then(function successCallback(response) {
		$scope.httplist = response.data.res;
		$rootScope.httplist = response.data.res;
		
		//data= {"id":"test","key":"key","username":"username"}
	}, function errorCallback(response) {
		$scope.httplist = "errorCallback: " + response.data;
	});
	
});
</script>
  
<div class="panel-heading">
<span ng-click='itemsClick'>items:{{items}}</span>
</div>

<div class="panel-footer">
	使用路由和$location切换视图 
</div>
<div class="panel-body" ng-view>
	


</div>
 
</div>
<script>

/*  provider（config） service factory 多次调用 缓存 多界面相同数据访问
服务是一个单例对象，在每个应用中只会被实例化一次（被$injector实例化），并且是延迟
加载的（需要时才会被创建）。服务提供了把与特定功能相关联的方法集中在一起的接口*/
// 创建一个模型用来支撑我们的购物视图
// 设置好服务工厂，用来创建我们的 Items 接口，以便访问服务端数据库
app.value('apiKey','123123123');	//单值固定 服务 不可注入
app.constant('apiKey2','123123123');//即可注入的常量
//常量可以注入到配置函数中，而值不行。 通常情况下，可以通过value()来注册服务对象或函数，用constant()来配置数据。
app.factory('Items', function($http, $q) {	//单例类 ？ 服务？ 通过Items 名获取到服务类引用 
   var items = {};  	//新建结果返回的类
   var name = "defaultName";	//类私有属性 静态？
   items.test = "items.test Items factory"; //类属性2
   //debugger;
   //类方法  // $q 类似同步数据访问的 service服务分离方式
   items.query = function(){  
		var deferred = $q.defer(); // 声明延后执行，表示要去监控后面的执行  
		$http({
			method: 'GET',
			url: '/BaseSSM/angular/get.do'
		}).then(
			function successCallback(response) {
				deferred.resolve(response.data);
			}, 
			function errorCallback(response) {
				deferred.resolve(response.data);
			}
		);
		return deferred.promise;   // 返回承诺，这里并不是最终数据，而是访问最终数据的API  
    };
	 
	items.setName = function(nname){	//类方法1
		name = nname;
		this.name = nname;
	}; 
	items.Name = name; 	//新属性 初始化设置值name
	items.getName = function(){return name;}  
   
   return items; //返回类引用
});
//service 单例模式 new Class 不return
app.service('serviceTest', function(){
	this.name = "1234";
	this.getService = function(){
		return "12345679";
	};
});

//diy过滤器  结合 ng-repeat实现本页面静态查询
app.filter('titleCase', function() {
  var str = function(input) {
      var words = input.split(' ');    
      for (var i = 0; i < words.length; i++) {
        words[i] = words[i].charAt(0).toUpperCase() + words[i].slice(1);    
      }    
      return words.join(' ');  
  };
  return str;   
});



function out(str){
	console.info(str);
}

 



</script>
 	

</div>
<%@ include file="/include/foot.jsp" %> 
