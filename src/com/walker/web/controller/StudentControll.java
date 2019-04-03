package com.walker.web.controller;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.walker.common.util.JsonUtil;
import com.walker.common.util.Page;
import com.walker.service.StudentService;
import com.walker.web.RequestUtil;


/**
 * 样例 student表 id-name-time属性 增删查改
 * 原始pw 和 跳转版本
 * @author Walker
 * 
 */  
@Controller
@RequestMapping("/student")
public class StudentControll {
	
	static public Logger logger = Logger.getLogger(StudentControll.class); 

	@Autowired
	@Qualifier("studentServiceHibernate") 
	StudentService studentServiceHibernate;

	@Autowired
	@Qualifier("studentServiceMybatis") 
	StudentService studentServiceMybatis;
	

	@RequestMapping("/listh.do")
	public String listh(HttpServletRequest request, Map<String,Object> map) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String timefrom = request.getParameter("timefrom");
		String timeto = request.getParameter("timeto");
		Page page = Page.getPage(request);
		map.putAll(RequestUtil.getRequestBean(request));
		
	    List<Map<String, Object>> res = studentServiceHibernate.list(id, name, timefrom, timeto, page);
	   // logger.info(MapListHelp.list2string(res));
		map.put("res", res);
		map.put("PAGE", page);
		return "student/list";
	}
	@RequestMapping("/updateh.do")
	public void updateh(HttpServletRequest request,  PrintWriter pw){
		 
		 
		
		String id = request.getParameter("id"); 
		String name = request.getParameter("name");
		String time = request.getParameter("time");
	    log(request); 
	    
		int res = studentServiceHibernate.update(id, name, time);
		pw.write("" + res);
	}
	@RequestMapping("/deleteh.do")
	public void deleteh(HttpServletRequest request,  PrintWriter pw){
		String id = request.getParameter("id");  
	    log(request);

		int res = studentServiceHibernate.delete(id );
	    pw.write("" + res);
	}
	@RequestMapping("/addh.do")
	public void addh(HttpServletRequest request,  PrintWriter pw){
		 
		 

		String name = request.getParameter("name");
		String time = request.getParameter("time");
	    log(request);

		int res = studentServiceHibernate.add(name, time);
	    pw.write("" + res);
	
	}
	@RequestMapping("/geth.do")
	public void geth(HttpServletRequest request,  PrintWriter pw){
		 
		 

		String id = request.getParameter("id");  
	    log(request);

		Map map = studentServiceHibernate.get(id );
		
	    pw.write("" + JsonUtil.makeJson(map));
	}
	   
	 
	@RequestMapping("/listm.do") 
	public String listm(HttpServletRequest request, Map<String,Object> map) {
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String timefrom = request.getParameter("timefrom");
		String timeto = request.getParameter("timeto");
	     
		Page page = Page.getPage(request);
		map.putAll(RequestUtil.getRequestBean(request));

	    List<Map<String, Object>> res = studentServiceMybatis.list(id, name, timefrom, timeto, page);
		map.put("PAGE", page);

	   // logger.info(MapListHelp.list2string(res));
		map.put("res", res );
 
		return "student/list";
	}
	@RequestMapping("/updatem.do")
	public void updatem(HttpServletRequest request,  PrintWriter pw){
		 
		 
		
		String id = request.getParameter("id"); 
		String name = request.getParameter("name");
		String time = request.getParameter("time");
	    log(request);

		int res = studentServiceMybatis.update(id, name, time);
		pw.write("" + res);
	}
	@RequestMapping("/deletem.do")
	public void deletem(HttpServletRequest request,  PrintWriter pw){
		 
		 
		
		String id = request.getParameter("id");  
	    log(request);

		int res = studentServiceMybatis.delete(id );
	    pw.write("" + res);
	}
	@RequestMapping("/addm.do")
	public void addm(HttpServletRequest request,  PrintWriter pw){
		 
		 

		String name = request.getParameter("name");
		String time = request.getParameter("time");
	    log(request); 
	    
		int res = studentServiceMybatis.add(name, time);
	    pw.write("" + res);
	
	}
	@RequestMapping("/getm.do")
	public void getm(HttpServletRequest request,  PrintWriter pw){
		 
		 

		String id = request.getParameter("id");  
	    log(request);

		Map map = studentServiceMybatis.get(id );
		
	    pw.write("" + JsonUtil.makeJson(map));
	}
	    
	 
	public void log(HttpServletRequest request){
	    //logger.info(WebHelp.getRequestBean(request).toString()); 
	}
}