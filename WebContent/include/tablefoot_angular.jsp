 <%@page pageEncoding="UTF-8"%>   
 
 <tfoot>
		<tr>
			<td colspan="998" ><center>
	<!-- 
	int eachPageNum = defaulteachPageNum;//每页数量
	int nowPage = 1;	//当前页码
	int pageNum = 0;	//总页数
	String order;	//排序
	String desc;	//倒序 
	-->
	 
				<button class="btn" ng-click="PAGE.nowPage=1;  list();">首页</button> 
				<button class="btn" ng-click="PAGE.nowPage=PAGE.nowPage-1;  list();">上一页</button> 
				<button class="btn" ng-click="PAGE.nowPage=PAGE.nowPage+ 1;  list(); ">下一页</button> 
				<button class="btn" ng-click="PAGE.nowPage=PAGE.pageNum;  list();">尾页</button> 
				当前第 
				<input type="text" name="nowPage" id="nowPage"  style="width:40px;" ng-model="PAGE.nowPage"    />
				/
				{{PAGE.pageNum}}页  
				每页显示  
				<input style="width: 40px;"  type="text" name="eachPageNum" id="eachPageNum"  ng-model="PAGE.eachPageNum"	  />
				条，共{{PAGE.num}}条 
				<input type="hidden" name="order" id="order" style="width:0px;height:0px;" value="{{PAGE.order}}"/>
			</center></td>
		</tr>
	</tfoot>
 
 
 