package com.controller;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mode.Page;
import com.service.StudentService;

import util.MyJson;
import util.Tools;


/**
 * 模板 继承至BaseControl的一套可用的增删查改操作
 */  
@Controller
@RequestMapping("/temp")
public class TempHibernateControll extends BaseControll{
	
	static public Logger logger = LoggerFactory.getLogger(TempHibernateControll.class); 

	@Autowired
	@Qualifier("studentServiceHibernate") 
	StudentService studentServiceHibernate;
 

	@RequestMapping("/list.do")
	public String list(HttpServletRequest request, Map<String,Object> map) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String timefrom = request.getParameter("timefrom");
		String timeto = request.getParameter("timeto");

		Page page = Page.getPage(request);
	    List<Map> res = studentServiceHibernate.list(id, name, timefrom, timeto, page);
		map.put("res", res);
		map.put("PAGE", page);
		return "student/list";
	}
	@RequestMapping("/update.do")
	public void update(HttpServletRequest request,  PrintWriter pw){
		String id = request.getParameter("id"); 
		String name = request.getParameter("name");
		String time = request.getParameter("time");
	    
		int res = studentServiceHibernate.update(id, name, time);
		pw.write("" + res);
	}
	@RequestMapping("/delete.do")
	public void delete(HttpServletRequest request,  PrintWriter pw){
		String id = request.getParameter("id");  

		int res = studentServiceHibernate.delete(id );
	    pw.write("" + res);
	}
	@RequestMapping("/add.do")
	public void add(HttpServletRequest request,  PrintWriter pw){
		String name = request.getParameter("name");
		String time = request.getParameter("time");

		int res = studentServiceHibernate.add(name, time);
	    pw.write("" + res);
	}
	@RequestMapping("/get.do")
	public void get(HttpServletRequest request,  PrintWriter pw){
		String id = request.getParameter("id");  

		Map map = studentServiceHibernate.get(id );
	    pw.write("" + MyJson.makeJson("obj", map));
	}
	
	
	@Override
	public void log(Object...objs) {
		logger.info(Tools.getString(objs));
	}
 
}