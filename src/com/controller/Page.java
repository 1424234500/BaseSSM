package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import util.Tools;

@Component
public class Page {
	static int defaultEachPageNum = 5;
	long NUM = 0;	//总数据条数
	int SHOWNUM = defaultEachPageNum;//每页数量
	int NOWPAGE = 1;	//当前页码
	int PAGENUM = 0;	//总页数
	String ORDER;	//排序
	String DESC;	//倒序
	Page(){}
	public Page(HttpServletRequest request){
		Page res = this;
		res.setNOWPAGE(request.getParameter("NOWPAGE"));
		res.setSHOWNUM(request.getParameter("SHOWNUM"));
		res.setORDER(request.getParameter("ORDER"));
		res.setDESC(request.getParameter("DESC"));
		
		if(res.getSHOWNUM() <= 0)
			res.setSHOWNUM(""+defaultEachPageNum);
		if(res.getNOWPAGE() <= 0)
			res.setNOWPAGE("1");
	}
	/**
	 * 通过request获取 查询第几页 每页多少条
	 */
	public static Page getPage(HttpServletRequest request){
		Page res = new Page();
		res.setNOWPAGE(request.getParameter("NOWPAGE"));
		res.setSHOWNUM(request.getParameter("SHOWNUM"));
		res.setORDER(request.getParameter("ORDER"));
		res.setDESC(request.getParameter("DESC"));
		
		if(res.getSHOWNUM() <= 0)
			res.setSHOWNUM(""+defaultEachPageNum);
		if(res.getNOWPAGE() <= 0)
			res.setNOWPAGE("1");
		return res;
	}
	public int getStart(){
		return (NOWPAGE-1) * SHOWNUM;
	}
	public int getStop(){
		return NOWPAGE * SHOWNUM;
	}
	public long getNUM() {
		return NUM;
	} 
	/**
	 * 设置预期数据的总数量 并根据页显示数量更新总页数 
	 * @param num
	 */
	public void setNUM(long num) {
		this.NUM = num;
		this.PAGENUM = (int) Math.ceil( 1.0 * num / this.SHOWNUM );
	}

	public int getSHOWNUM() {
		return SHOWNUM;
	}

	public void setSHOWNUM(String eachPageNum) {
		this.SHOWNUM = Tools.parseInt(eachPageNum);
	}

	public int getNOWPAGE() {
		return NOWPAGE;
	}

	public void setNOWPAGE(String nowPage) {
		this.NOWPAGE = Tools.parseInt(nowPage);
	}

	public int getPAGENUM() {
		return PAGENUM;
	}

	public void setPAGENUM(String pageNum) {
		this.PAGENUM = Tools.parseInt(pageNum);
	}

	public String getORDER() {
		return ORDER;
	}

	public void setORDER(String order) {
		this.ORDER = order;
	}

	public String getDESC() {
		return DESC;
	}

	public void setDESC(String desc) {
		this.DESC = desc;
	}
	
	
	
}