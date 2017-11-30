package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mode.LoginUser;
import com.mode.Page;
import com.service.StudentService;

import util.MakeMap;
import util.MapListHelp;
import util.MyJson;
import util.Tools;
import util.WebHelp;

/** 
 * 测试AngularJs的后台
 * @author Walker
 *
 */
@Controller
@RequestMapping("/angular")
public class AngularControll extends BaseControll{   
	@Autowired
	@Qualifier("studentServiceHibernate") 
	StudentService studentServiceHibernate;

	
	@RequestMapping("/login.do") 
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		Map res = MapListHelp.getMap().put("res", "true").put("info",WebHelp.getRequestMap(request)).build(); 
		log(res);
		writeJson(response, res);
	}	
	 
	@RequestMapping("/update.do")
	public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("ID"); 
		String name = request.getParameter("NAME");
		String time = request.getParameter("TIME");
	    
	    Map res = null;
	    if(studentServiceHibernate.get(id).isEmpty()){
	    	res = MapListHelp.getMap().put("res",studentServiceHibernate.add(name, time)).build();
	    }else{
	    	res = MapListHelp.getMap().put("res",studentServiceHibernate.update(id, name, time)).build();
	    }
		writeJson(response, res);
	}
	@RequestMapping("/delete.do")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("ID");
	    
	    Map res = MapListHelp.getMap().put("res",studentServiceHibernate.delete(id)).build();
		writeJson(response, res);
	}	
	@RequestMapping("/get.do")
	public void get(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//Map res = MapListHelp.getMap().put("id", "id-001").put("key", "key-001").put("username", "username-001").build();
		String id = request.getParameter("id");   
		Map res = studentServiceHibernate.get(id );
		log(res); 
		writeJson(response, res);
	}	
	@RequestMapping("/list.do")
	public void list(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("ID");
		String name = request.getParameter("NAME");
		String timefrom = request.getParameter("TIMEFORM");
		String timeto = request.getParameter("TIMETO");
		
		Page page = getPage(request);
		List<Map> list = studentServiceHibernate.list(id, name, timefrom, timeto, page);
		log(list, page);
		writeJson(response, list, page);
	}
	
	
	static public Logger logger = LoggerFactory.getLogger(AngularControll.class); 

	@Override
	public void log(Object... objs) {
		 logger.info(Tools.getString(objs));
	}
    
}