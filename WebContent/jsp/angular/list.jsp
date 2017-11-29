<%@page pageEncoding="UTF-8"%>   
   

  

 	
<div class="panel-heading"> 
	<h4>学生信息管理 </h4>  

            <div class="collapse navbar-collapse"  >
                <form class="navbar-form navbar-left" >
                        <input type="text" ng-model="search.ID" class="form-control" placeholder="search.ID">
                        <input type="text" ng-model="search.NAME" class="form-control" placeholder="search.NAME">
                        <input type="text" ng-model="search.TIMEFROM" id="timefrom" data-date-format="yyyy-mm-dd hh:ii" class="form-control" placeholder="search.TIMEFROM">
                        <input type="text" ng-model="search.TIMETO" id="timeto" data-date-format="yyyy-mm-dd hh:ii" class="form-control" placeholder="search.TIMETO">
 <!-- 
                        <input type="text" ng-model="search.TIMEFROM" class="form-control" placeholder="search.TIMEFROM" onFocus="WdatePicker({readOnly:false,dateFmt:'yyyy-MM-dd HH:mm:ss'})" >
                        <input type="text" ng-model="search.TIMETO" class="form-control" placeholder="search.TIMETO" onFocus="WdatePicker({readOnly:false,dateFmt:'yyyy-MM-dd HH:mm:ss'})" >
 -->                   	   
                   	    <button class="btn" ng-click="list()" >查询</button>
                </form>
            </div>
</div> 
	  <!-- javascript:void(0) --> 
     <a class="btn" href="/BaseSSM/jsp/angular/page.jsp#/add"  >添加</a>  
	 
<div class="panel-footer"> </div>


<div class="panel-body">
	<table class="table">
				<thead>
					<tr>
						<th> No. </th>
						<th ng-click="changeOrder('ID')" ng-class="{dropup:order === ''}">
                 	  		id
                  		    <span ng-class="{orderColor:orderType === 'ID'}" class="caret"></span>
                		</th>
                		<th ng-click="changeOrder('NAME')" ng-class="{dropup:order === ''}">
                 	  		name
                  		    <span ng-class="{orderColor:orderType === 'NAME'}" class="caret"></span>
                		</th>
                		<th ng-click="changeOrder('TIME')" ng-class="{dropup:order === ''}">
                 	  		time
                  		    <span ng-class="{orderColor:orderType === 'TIME'}" class="caret"></span>
                		</th> 
						<th> update </th>
						<th> delete </th>
					</tr>
				</thead>
				<tbody id="tableContent">
				
  <!-- | filter:search 查询功能 默认 filterQuery自定义过滤器 --> 
<tr ng-repeat="item in httplist | filter:search | filterQuery:search  |    orderBy:order + orderType"  ng-click='selectRowFun($index)' class='{menu-true: $index==selectedRow}' >   
			<td>{{$index + 1}}</td>   <!--  $index 在表格中显示出行号， -->
		    <td class="info">{{item.ID}}</td>  
		    <td class="warning">{{item.NAME }}</td>
		    <td class="error">{{item.TIME | date:'medium'}}</td>
		    <td> 
			     <a class="btn" href="/BaseSSM/jsp/angular/page.jsp#/update/{{item.ID}}"  > 修改</a>  
		    </td>
		    <td>
			     <a class="btn" href="" ng-click="ajaxDelete(item.ID)" > 删除</a>  
		    </td>
</tr>  
				</tbody>
				
				<%@ include file="/include/tablefoot_angular.jsp" %>  

	</table>  
	data.res={{httplist}}<br>
	data.PAGE={{PAGE}}<br>
	testPromiseQ = {{testPromiseQ}}
</div>
 


