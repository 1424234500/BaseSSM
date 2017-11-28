<%@page pageEncoding="UTF-8"%>   



<div class="panel-heading">
	更新{{httpget}}
</div>

<div class="panel-body">

            <div class="collapse navbar-collapse">
                <form class="navbar-form navbar-left"  >
		<fieldset>
		<label>id</label> <br>
		<!--  	
		 ng-bind=={{}}是从$scope -> view的单向绑定
		 ng-modle是$scope <-> view的双向绑定 
		 -->
		<input id="id" name="id"  type="text" class="form-control"  readonly="readonly"  ng-model="httpget.ID" value=""/> <br>
		<label>name</label> <br>
		<input id="name" name="name"  class="form-control"  type="text"   ng-model="httpget.NAME" /> <br>
		<label>time</label> <br>
		<!-- datepicker的修改事件不能触发ng-model的绑定事件，需要手动输入或修改   -->
<input type="text" ng-model="httpget.TIME" id="time" data-date-format="yyyy-mm-dd hh:ii" class="form-control" placeholder="">
		 
		 <br>
		</fieldset>
	
</form></div></div>
		
<div class="panel-footer">
	<button ng-click="ajaxUpdate()"  class="btn">提交</button>
	<button id="gopage" class="btn">关闭</button>
</div> 