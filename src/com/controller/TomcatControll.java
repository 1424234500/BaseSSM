package com.controller;

import java.io.IOException;
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

import com.mode.Page;
import com.service.StudentService;

import util.MapListHelp;
import util.Tools;
import util.WebHelp;

/** 
 * Tomcat监控后台
 * @author Walker
 *
 */
@Controller
@RequestMapping("/tomcat")
public class TomcatControll extends BaseControll{    
  
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/statis.do") 
	public void statis(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int yearFrom = Tools.parseInt(request.getParameter("TIMEFROM"));
		int yearTo = Tools.parseInt(request.getParameter("TIMETO")); 
		
	 	List list = MapListHelp.toArrayAndTurn(baseService.find("SELECT url,cast(avg(costtime/1000) as number(8,3)) time FROM log_time where 1=1 group by url order by avg(costtime) ")) ;
		List listLineNames = MapListHelp.array().add("action").build();
		List listSeries =  MapListHelp.array().add((List) list.get(1)).build();
		String type = "bar";	 
		Map title = MapListHelp.map().put("text", "操作耗时").build();		//标题
		Map legend = MapListHelp.map().put("data", listLineNames).build();   //线条名字集合
		Map xAxis = MapListHelp.map().put("data", (List) list.get(0)).build();  	//x坐标集合 多线条共x轴
		List series = MapListHelp.array().build();
		for(int i = 0; i < listSeries.size(); i++){
			//type = i / 2 == 0 ? "bar" : "line"; 
			series.add(MapListHelp.map()
					.put("name", listLineNames.get(i))	//该线条的名字
					.put("type", type)					//该线条的显示方式line bar pie
					.put("data", listSeries.get(i))			//该线条的y值集合
					.build()
				);
		} 
		Map option = MapListHelp.map()
				.put("title", title)  
				.put("legend", legend) 
				.put("xAxis", xAxis) 
				.put("yAxis", new Object()) //必须要有yAxis属性 否则报错YAxis 0 not found
				.put("series", series) 
				.build();
		 

		Map res = MapListHelp.getMap()
				.put("res", "true")
				.put("option", option) 
				.put("info", WebHelp.getRequestMap(request)).build(); 
		log(res);
		writeJson(response, res);
	}	
	
	 
	
	
	static public Logger logger = LoggerFactory.getLogger(TomcatControll.class); 

	@Override
	public void log(Object... objs) {
		 logger.info(Tools.getString(objs));
	}
    
}