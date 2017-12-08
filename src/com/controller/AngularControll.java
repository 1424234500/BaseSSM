package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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

	

	
	@RequestMapping("/statis.do") 
	public void statis(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		
		   
		List<Map> list = baseService.find("SELECT t.xs x,count(*) y FROM ( SELECT s.*,to_char(s.time, 'MM') xs FROM student s) t group by t.xs");
		list = MapListHelp.turnListMap(list);
		log(list);
		//共x轴 多线数据 
		//x1, x2, x3, x4
		//y1, y2, y3, y4 
		//y5, y6, y7, y8
		
		//线条名字集合
		List<String> lineNames = new ArrayList<String>();
		lineNames.add("线条1");
		lineNames.add("线条2");
		

		Map xNames = new HashMap();
		line1.put("name", "线条2");
		line1.put("type", "bar");
		line1.put("data", list.get(1)); 
		//每个线条的数据 和 配置
		List<Map> lines = new ArrayList<Map>();
		Map line1 = new HashMap();
		line1.put("name", "线条1");
		line1.put("type", "line");
		line1.put("data", list.get(1)); 
		Map line2 = new HashMap();
		line1.put("name", "线条2");
		line1.put("type", "bar");
		line1.put("data", list.get(1)); 
		lines.add(line1);
		lines.add(line2);
		
		Map option = MapListHelp.getMap()
				.put("title", "每月注册人数统计")  
				.put("lineNames", lineNames) 
				.put("lines", lines) 
				.build();
		
		log(list);

		Map res = MapListHelp.getMap()
				.put("res", "true")
				.put("option", option) 
				.put("info",WebHelp.getRequestMap(request)).build(); 
		log(res);
		writeJson(response, res);
	}	
	
	
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