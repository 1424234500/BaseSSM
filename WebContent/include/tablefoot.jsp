 <%@page pageEncoding="UTF-8"%>   
 
 <tfoot>
		<tr>
			<td colspan="10">
	<!-- 
	int eachPageNum = defaulteachPageNum;//每页数量
	int nowPage = 1;	//当前页码
	int pageNum = 0;	//总页数
	String order;	//排序
	String desc;	//倒序 
	-->
	 
				<button class="btn" onclick="getElementById('nowPage').value=1;   getElementById('myform').submit();  ">首页</button> 
				<button class="btn" onclick="var nowPage=parseInt('${requestScope.PAGE.nowPage}');if(nowPage >  1) {getElementById('nowPage').value=nowPage-1;getElementById('myform').submit();} ;">上一页</button> 
				<button class="btn" onclick="var nowPage = parseInt( '${requestScope.PAGE.nowPage}');	var numPage = parseInt('${requestScope.PAGE.pageNum}'); if(nowPage < numPage){ getElementById('nowPage').value=nowPage+ 1; getElementById('myform').submit();}   ">下一页</button> 
				<button class="btn" onclick="getElementById('nowPage').value='${requestScope.PAGE.pageNum }';    getElementById('myform').submit();  ">尾页</button> 
				当前第 
				<input type="text" name="nowPage" id="nowPage"  style="width:40px;" value="${ requestScope.PAGE.nowPage}" onblur="var numPage = parseInt('${requestScope.PAGE.pageNum}'); num=this.value;if(num<1){this.value='1';} if(num>numPage){this.value=numPage;}" />
				/
				${requestScope.PAGE.pageNum }页  
				每页显示  
				<input style="width: 40px;"  type="text" name="eachPageNum" id="eachPageNum" 	value="${requestScope.PAGE.eachPageNum }" />
				条，共${ 	requestScope.PAGE.num}条 
				<input type="hidden" name="order" id="order" style="width:0px;height:0px;" value="${requestScope.PAGE.order }"/>
			</td>
		</tr>
	</tfoot>
 
 
 