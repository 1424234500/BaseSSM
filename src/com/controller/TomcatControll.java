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
		String url = request.getParameter("URL"); 
		
	 	List list = null;
	 	if(Tools.isNull(url)){
	 		list = MapListHelp.toArrayAndTurn(baseService.find(" SELECT lev, nvl(time, '0') time FROM  ( SELECT hour, cast(sum(costtime)/sum(count)/1000 as number(8, 3)) time FROM (  SELECT  to_char(lt.time, 'hh24') hour, lt.count, lt.costtime FROM log_time lt where 1=1 and lt.url=?  )group by hour   ) t1,  ( select lpad(level, 2, '0') lev from dual connect by level <= 24    ) t2 where t1.hour(+) = t2.lev  ORDER BY lev ", url)) ;
	 	}else{
	 		list = MapListHelp.toArrayAndTurn(baseService.find("SELECT url,cast(sum(costtime)/sum(count)/1000 as number(8, 3)) time FROM log_time where 1=1 group by url order by url ")) ;
	 	} 
		 
	 	 
	 	List listLineNames = MapListHelp.array().add("action").build();
		List listSeries =  MapListHelp.array().add(list.size() > 0 ? (List) list.get(1) : new Object()).build();
		String type = "bar";	 
		Map title = MapListHelp.map().put("text", "操作耗时统计").build();		//标题
		Map legend = MapListHelp.map().put("data", listLineNames).build();   //线条名字集合
		Map xAxis = MapListHelp.map().put("data", list.size() > 0 ? (List) list.get(0) : new Object()).build();  	//x坐标集合 多线条共x轴
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
				.put("tooltip", new Object())  
				.put("xAxis", xAxis) 
				.put("yAxis", new Object()) //若无报错YAxis 0 not found
				.put("series", series) 
				.build();
		 

		Map res = MapListHelp.getMap()
				.put("res", "true")
				.put("option", option) 
				.put("info", WebHelp.getRequestMap(request)).build(); 
		log(res);
		writeJson(response, res);
	}	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/statiscount.do") 
	public void statiscount(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String url = request.getParameter("URL"); 
		
	 	List list = null;
	 	if(Tools.isNull(url)){
	 		list = MapListHelp.toArrayAndTurn(baseService.find("  SELECT lev, nvl(count, '0') sumcount FROM  ( SELECT hour, sum(count) count FROM (  SELECT  to_char(lt.time, 'hh24') hour, lt.url, lt.count FROM log_time lt where 1=1 and lt.url=?  )group by hour ) t1,  ( select lpad(level, 2, '0') lev from dual connect by level <= 24    ) t2 where t1.hour(+) = t2.lev  ORDER BY lev ", url)) ;
	 	}else{
	 		list = MapListHelp.toArrayAndTurn(baseService.find(" SELECT url,sum(count) sumcount FROM log_time where 1=1 group by url order by url ")) ;
	 	}  
		
	 	 
	 	List listLineNames = MapListHelp.array().add("action").build();
		List listSeries =  MapListHelp.array().add(list.size() > 0 ? (List) list.get(1) : new Object()).build();
		String type = "bar";	 
		Map title = MapListHelp.map().put("text", "操作频率统计").build();		//标题
		Map legend = MapListHelp.map().put("data", listLineNames).build();   //线条名字集合
		Map xAxis = MapListHelp.map().put("data", list.size() > 0 ? (List) list.get(0) : new Object()).build();  	//x坐标集合 多线条共x轴
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
				.put("tooltip", new Object()) 
				.put("xAxis", xAxis) 
				.put("yAxis", new Object()) //若无报错YAxis 0 not found
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