package com.walker.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.walker.common.util.MapListUtil;
import com.walker.common.util.Tools;
import com.walker.web.RequestUtil;

@Controller
@RequestMapping("/lunch")
public class LunchControl extends BaseControll {
	
	public LunchControl() {
		super(LunchControl.class, "lunch");
	} 
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/statis.do") 
	public void statis(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		  
//		        legend: {      data: ['线条1', '线条2']   },
//		        xAxis: {  data: xNames  }, 
//		        series: [   
//		                 { name: lineName,    type: 'line',   data: lineValues,  },
//		                 { name: lineName,    type: 'line',   data: lineValues,  }, 
		
		//x1, x2, x3, x4

		String timeFrom = (request.getParameter("TIMEFROM"));//yyyy-mm
		String timeTo = (request.getParameter("TIMETO")); 
		Date date = new Date();
		timeFrom = Tools.notNull(timeFrom)? timeFrom : Tools.getTime("yyyy-MM");
		timeTo = Tools.notNull(timeTo)? timeTo :  Tools.getTime("yyyy-MM");
		int maxMonthDay = 31;
		int monthFrom = Tools.parseInt(timeFrom.substring(5, 7));
		int monthTo = Tools.parseInt(timeTo.substring(5, 7));
		List listXs = MapListUtil.toArrayAndTurn(baseService.find("select lpad(level, 2, '0') lev from dual connect by level <=?", maxMonthDay)).get(0);
		List listLineNames = MapListUtil.array().build();

		List listSeries = MapListUtil.array().build(); 
		for(int i = monthFrom; i <= monthTo; i++){
			//yi-1, yi-2, yi-3, yi-4
			listSeries = MapListUtil.listAdd(listSeries,  MapListUtil.toArrayAndTurn(baseService.find(
					" SELECT "
//					+ " t2.lev x, "
					+ " nvl(t1.y, '0') y "
					+ "FROM (SELECT substr(t.xs,4,2) x,sum(t.buynum) y FROM (   "
					+ "     SELECT s.*,substr(s.day,6,5) xs FROM lunch s where substr(s.day,6,2)=lpad(?, 2, '0') and substr(s.day,1,7)>=? and substr(s.day,1,7)<=? "
					+ ") t group by t.xs ) t1,(select lpad(level, 2, '0') lev from dual connect by level <=?   ) t2 where t1.x(+) = t2.lev order by t2.lev  "
					, i, timeFrom, timeTo, maxMonthDay)) ); 
			listLineNames.add(i + "月"); 
		}
		//共x轴 多线数据 
		//x1, x2, x3, x4
		//y1, y2, y3, y4 
		//y5, y6, y7, y8   
		String type = "bar";	
		Map title = MapListUtil.map().put("text", "午饭买票统计").build();		//标题
		Map legend = MapListUtil.map().put("data", listLineNames).build();   //线条名字集合
		Map xAxis = MapListUtil.map().put("data", listXs).build();  	//x坐标集合 多线条共x轴
		List series = MapListUtil.array().build();
		for(int i = 0; i < listSeries.size(); i++){
			//type = i / 2 == 0 ? "bar" : "line"; 
			series.add(MapListUtil.map()
					.put("name", listLineNames.get(i))	//该线条的名字
					.put("type", type)					//该线条的显示方式line bar pie
					.put("data", listSeries.get(i))			//该线条的y值集合
					.build()
				);
		} 
		Map option = MapListUtil.map()
				.put("title", title)  
				.put("legend", legend) 
				.put("tooltip", new HashMap()) //若无则不能预览
				.put("xAxis", xAxis) 
				.put("yAxis", new HashMap()) //若无则报错YAxis 0 not found
				.put("series", series) 
				.build();
		 

		Map res = MapListUtil.getMap()
				.put("res", "true")
				.put("option", option) 
				.put("info", RequestUtil.getRequestBean(request)).build(); 
		log(res);
		echo(res);
	}	
	
	
}