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
import org.springframework.web.bind.annotation.RequestMapping;

import com.mode.LoginUser;
import com.service.BaseService;

import util.Bean;
import util.JsonUtil;
import util.MapListUtil;
import util.Tools;
import util.database.SqlHelp;


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
	    List<Map<String, Object>> res = baseService.findPage(page, "select * from student");
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
/**
* {
* 	"flag":true,
* 	"info":"网络情况良好",
* 	"timestart":"324234",
* 	"timestop":"101992",
* 	"time":"耗时10020020292s",
* 	"data":{}/[]
* }
 * @throws IOException 
*/
	public void echo(Object data) throws IOException{
		echo(true, "", data);
	}
	public void echoPage(List<?> list, Page page) throws IOException{
		Bean data = Bean.getBean().put("list", list).put("page", page);
		echo(true, "", data);
	}
	public void echo(boolean flag, String info, Object data) throws IOException{
		HttpServletResponse response = Context.getResponse();
		long timestart = Context.getTimeStart();
		long timestop = System.currentTimeMillis();
		long time = timestop - timestart;
		Bean bean = Bean.getBean()
				.put("flag", flag)
				.put("info", info)
				.put("timestart", timestart)
				.put("timestop", timestop)
				.put("time", time)
				.put("data", data);
		String jsonStr = (JsonUtil.makeJson(bean));
		response.getWriter().write( jsonStr );
	}
	
	/**
	 * 登录用户获取LoginUser
	 */
	public LoginUser getUser(){
		HttpServletRequest request = Context.getRequest();

		LoginUser lu = (LoginUser) request.getSession().getAttribute("SY_LOGINUSER");
		return lu == null? LoginUser.getUser().setId("").setUsername("none") : lu;
	}
	/**
	 * 设置默认操作表 实现默认增删查改 单主键
	 * 设置子类日志系统
	 * @param clazz
	 * @param tableName
	 */ 
	public BaseControll(Class<?> clazz, String tableName){
		Context.setTableName(tableName);
		Context.set("LOGGER", LoggerFactory.getLogger(clazz));
	}
	/**
	 * 实现日志打印工具
	 * @param str
	 */
	public void log(Object...objs){
		Logger log = (Logger) (Context.get("LOGGER"));
		if(log != null)
			log.info(Tools.objects2string(objs));
		else
			logger.info(Tools.objects2string(objs));
	}
	
	
	/**
	 * 获取list add update 时的 需要的 传过来的所有 该表的字段param map 表字段列名 大写为准
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public Map getTableParam(HttpServletRequest request) throws Exception{
		if(!Tools.notNull(getTableName()))
			throw new Exception("没有配置表");
		List<Object> res = baseService.getColumns(getTableName()); 
		return WebHelp.getParam(request, res);
	}
	public String getValue(HttpServletRequest request, String key) {
		return WebHelp.getKey(request, key);
	}
	/**
	 * 获取表键名
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String getTableKeyName(HttpServletRequest request) throws Exception{
		if(!Tools.notNull(getTableName()))
			throw new Exception("没有配置表");
		List<Object> res = baseService.getColumns(getTableName()); 
		if(res.size() <= 0)
			throw new Exception("该表 " + getTableName() + " 没有列 ");
		return (String)res.get(0);
	}
	@SuppressWarnings("rawtypes")
	@RequestMapping("/list.do")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.beforeDo(request, response);
		
		Map<String, Object> map = this.getTableParam(request);
		Page page = Page.getPage(request);

		List<String> params = new ArrayList<String>();
		
		String sql = "select * from " + getTableName() + " where 1=1 ";
		
		for(String key : map.keySet()){
			String value = MapListUtil.getMap(map, key);
			if (Tools.notNull(value)) {
				sql += " and " + key + " like ? ";
				params.add("%" + value + "%");
			}
		}
		 
		List<Map<String, Object>> res = baseService.findPage(page, sql, params.toArray());
		log(res.size(), res, page);
		writeJson(response, res, page);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/delete.do")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.beforeDo(request, response);

		String key = this.getTableKeyName(request);
		String value = this.getValue(request, key);

		int count = baseService.executeSql("delete from " + getTableName() + " where " + key + "=?", value);
		Map res = MapListUtil.getMap().put("res", count).build();
		writeJson(response, res);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/add.do")
	public void add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.beforeDo(request, response);

		Map map = this.getTableParam(request);

		int count = baseService.executeSql("insert into " + getTableName() + "(" + SqlHelp.makeMapKeys(map) + ") values(" + SqlHelp.makeMapPosis(map) + ")  ", map.values().toArray());
		Map res = MapListUtil.getMap().put("res", count).build();
		writeJson(response, res);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/update.do")
	public void update(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.beforeDo(request, response);

		Map map = this.getTableParam(request);
		String key = this.getTableKeyName(request);
		String value = this.getValue(request, key); 
		String cols = SqlHelp.makeMapKeyPosis(map);
		List<String> params = new ArrayList<String>(map.values());
		params.add(value);
		int count = baseService.executeSql("update " + getTableName() + " set " + cols + " where " + key + "=?", params.toArray());
		Map res = MapListUtil.getMap().put("res", count).build();
		writeJson(response, res);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/get.do")
	public void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.beforeDo(request, response);

		String key = this.getTableKeyName(request);
		String value = this.getValue(request, key);
		
		Map map = baseService.findOne("select * from " + getTableName() + " where " + key + "=?", value);
		writeJson(response, map);
	}
	
	
	@RequestMapping("/cols.do")
	public void cols(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.beforeDo(request, response);

		if(!Tools.notNull(getTableName()))return;
		List<Object> res = baseService.getColumns(getTableName());
		writeJson(response, res);
	}
	
	void beforeDo(HttpServletRequest request, HttpServletResponse response) {
		String tableName = this.getValue(request, "TABLE_NAME");
		if(Tools.notNull(tableName)){
			this.setTableName(tableName);
		}
	} 
	

	
}