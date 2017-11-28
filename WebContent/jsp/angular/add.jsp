<%@page pageEncoding="UTF-8"%>   



<div class="panel-heading">
	添加
</div>

<div class="panel-body">

            <div class="collapse navbar-collapse">
                <form class="navbar-form navbar-left"  >
                
		<fieldset>
		<!--  	
		 ng-bind=={{}}是从$scope -> view的单向绑定
		 ng-modle是$scope <-> view的双向绑定 
		 -->
		<label>name</label> <br>
		<input id="name" name="name"  type="text"   ng-model="httpget.NAME" class="form-control"/> <br>
		<label>time</label> <br>
		<!-- datepicker的修改事件不能触发ng-model的绑定事件，需要手动输入或修改   -->
<input type="text" ng-model="httpget.TIME" id="time" data-date-format="yyyy-mm-dd hh:ii" class="form-control" placeholder="">
		 <br>
		</fieldset>
		
	</form>
	</div>
</div>
	
<div class="panel-footer">
	<button ng-click="ajaxUpdate()"  class="btn">提交</button>
	<button id="gopage" class="btn">关闭</button>
</div> 