package com.mode;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import util.Tools;

@Component
public class Page {
	static int defaultEachPageNum = 5;
	long num = 0;	//总数据条数
	int eachPageNum = defaultEachPageNum;//每页数量
	int nowPage = 1;	//当前页码
	int pageNum = 0;	//总页数
	String order;	//排序
	String desc;	//倒序
	Page(){}
	public Page(HttpServletRequest request){
		Page res = this;
		res.setNowPage(request.getParameter("nowPage"));
		res.setEachPageNum(request.getParameter("eachPageNum"));
		res.setOrder(request.getParameter("order"));
		res.setDesc(request.getParameter("desc"));
		
		if(res.getEachPageNum() <= 0)
			res.setEachPageNum(""+defaultEachPageNum);
		if(res.getNowPage() <= 0)
			res.setNowPage("1");
	}
	/**
	 * 通过request获取 查询第几页 每页多少条
	 */
	public static Page getPage(HttpServletRequest request){
		Page res = new Page();
		res.setNowPage(request.getParameter("nowPage"));
		res.setEachPageNum(request.getParameter("eachPageNum"));
		res.setOrder(request.getParameter("order"));
		res.setDesc(request.getParameter("desc"));
		
		if(res.getEachPageNum() <= 0)
			res.setEachPageNum(""+defaultEachPageNum);
		if(res.getNowPage() <= 0)
			res.setNowPage("1");
		
		return res;
	}
	public int getStart(){
		return (nowPage-1) * eachPageNum;
	}
	public int getStop(){
		return nowPage * eachPageNum;
	}
	public long getNum() {
		return num;
	} 
	/**
	 * 设置预期数据的总数量 并根据页显示数量更新总页数 
	 * @param num
	 */
	public void setNum(long num) {
		this.num = num;
		this.pageNum = (int) Math.ceil( 1.0 * num / this.eachPageNum );
	}

	public int getEachPageNum() {
		return eachPageNum;
	}

	public void setEachPageNum(String eachPageNum) {
		this.eachPageNum = Tools.parseInt(eachPageNum);
	}

	public int getNowPage() {
		return nowPage;
	}

	public void setNowPage(String nowPage) {
		this.nowPage = Tools.parseInt(nowPage);
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(String pageNum) {
		this.pageNum = Tools.parseInt(pageNum);
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	
	
}