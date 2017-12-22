package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mode.LoginUser;
import com.mode.Page;
import com.service.BaseService;

import util.MapListHelp;
import util.MyJson;


/**
 * Control基类   提供常用模板操作案例 一些工具
 * @author Walker
 *
 */  
public abstract class BaseControll {

	static public Logger logger = LoggerFactory.getLogger(BaseControll.class); 

	/**
	 * 基本的通用型 hibernate查询service
	 */
	@Autowired
	@Qualifier("baseService") 
	protected BaseService baseService;
	
	/**
	 * 普通跳转模式，request获取参数，map写入参数<request.setAttrbute()>，或者request.getSession().setAttrbute()写入session
	 */
	@RequestMapping("/testlist.do")
	public String testList(HttpServletRequest request, Map<String,Object> map) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String timefrom = request.getParameter("timefrom");
		String timeto = request.getParameter("timeto");
		Page page = Page.getPage(request);
	    List<Map> res = baseService.findPage(page, "select * from student");
		map.put("res", res);
		map.put("PAGE", page);
		return "student/list";
	}
	/**
	 * Ajax模式，request获取参数，需要PrintWriter写入参数 
	 */
	@RequestMapping("/testupdate.do")
	public void testAjaxUpdateh(HttpServletRequest request, PrintWriter pw){
		String id = request.getParameter("id"); 
		String name = request.getParameter("name");
		String time = request.getParameter("time");
		
		int res = baseService.executeSql(
				"update student set name=?,time=to_date(?,'yyyy-mm-dd hh24:mi:ss') where id=?"
				,name,time,id);

		pw.write("" + res);
	} 
	

	public void writeJson(HttpServletResponse response, String key, List list) throws IOException{
		writeJson(response, MyJson.makeJson(key, list) ); 
	}
	public void writeJson(HttpServletResponse response, String key, Map map) throws IOException{
		writeJson(response, MyJson.makeJson(key, map) ); 
	}

	public void writeJson(HttpServletResponse response, List list) throws IOException{
		writeJson(response, MyJson.makeJson(list) ); 
	}
	public void writeJson(HttpServletResponse response, Map map) throws IOException{
		writeJson(response, MyJson.makeJson(map) ); 
	}
	public void writeJson(HttpServletResponse response, List<Map> list, Page page) throws IOException{
		Map res = MapListHelp.getMap().put("res", list).put("PAGE", page).build();
		writeJson(response, res);
	}
	public void writeJson(HttpServletResponse response, String jsonStr) throws IOException{
		response.getWriter().write(jsonStr); 
	}
	/**
	 * 分页参数获取Page
	 */
	public Page getPage(HttpServletRequest request){
		return new Page(request);
	}
	/**
	 * 登录用户获取LoginUser
	 */
	public LoginUser getUser(HttpServletRequest request){
		LoginUser lu = (LoginUser) request.getSession().getAttribute("SY_LOGINUSER");
		return lu == null? LoginUser.getUser().setId("").setUsername("none") : lu;
	}
	/**
	 * 实现日志打印工具
	 * @param str
	 */
	public abstract void log(Object...objs);
	
	
}