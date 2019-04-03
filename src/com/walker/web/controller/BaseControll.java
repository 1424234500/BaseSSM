package com.walker.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;

import com.walker.common.util.Bean;
import com.walker.common.util.JsonUtil;
import com.walker.common.util.MapListUtil;
import com.walker.common.util.Page;
import com.walker.common.util.Tools;
import com.walker.common.util.XmlUtil;
import com.walker.core.cache.Cache;
import com.walker.core.cache.CacheMgr;
import com.walker.core.database.SqlHelp;
import com.walker.service.BaseService;
import com.walker.web.RequestUtil;
import com.walker.web.mode.LoginUser;


/**
 * Control基类   提供常用模板操作案例 一些工具
 * @author Walker
 *
 */  
public abstract class BaseControll {
	private static final String RES_TYPE = "_RES";
	private static final String TYPE_XML = "XML";
	private static final String TYPE_JSON = "JSON";

	/**
	 * 基本的通用型 hibernate查询service
	 */
	@Autowired
	@Qualifier("baseService") 
	protected BaseService baseService;
 
	/**
	 * 内存缓存map
	 */
	protected Cache<String> cache = CacheMgr.getInstance();
	
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
	public void echo(List<?> list, Page page) throws IOException{
		Bean data = Bean.getBean().put("list", list).put("page", page);
		echo(true, "", data);
	}
	public void echo(boolean flag, String info) throws IOException{
		echo(flag, info, "");
	}
	public static char[] asciiNo = null;
	static {
		asciiNo = new char[32];
		for(int i = 0; i < 32; i++){
			asciiNo[i] = (char)i;
		}
	}
	public void echo(boolean flag, String info, Object data) throws IOException{
		HttpServletRequest request = Context.getRequest();
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
		
		String res = "";
		String resType = getValue(request, RES_TYPE).toUpperCase();
		if(resType.equals(TYPE_XML)){
			res = XmlUtil.toFullXml(bean);
		}else{
			res = (JsonUtil.makeJson(bean));
			//过滤特殊字符避免ie解析json异常
			res = res.replace("[\\x00-\\x1f]+", "");
		}
		
		response.getWriter().write( res );
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
		this.tableName = tableName;
		this.logger = Logger.getLogger(clazz);
	}
	
	private String tableName = ""; //成员变量 单例 公用 于 每个并发线程
	private Logger logger = Logger.getLogger(BaseControll.class); //成员变量 单例 公用 于 每个并发线程

	/**
	 * 实现日志打印工具
	 * @param str
	 */
	public void log(Object...objs){
		Logger log = Context.get("LOGGER", this.logger);
		log.info(Tools.objects2string(objs));
	}
	
	
	/**
	 * 获取list add update 时的 需要的 传过来的所有 该表的字段param map 表字段列名 大写为准
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("rawtypes")
	public Map getTableParam(HttpServletRequest request) throws Exception{
		String tableName = getTableName();
		if(!Tools.notNull(tableName))
			throw new Exception("没有配置表");
		List<String> res = baseService.getColumns(tableName); 
		return RequestUtil.getParam(request, res);
	}
	/**
	 * 获取request key 兼容大小写
	 */
	public String getValue(HttpServletRequest request, String key) {
		return RequestUtil.getKey(request, key);
	}
	
	/**
	 * 站内跳转 jsp || servlet
	 */
	public void sendDispatcher(HttpServletRequest request, HttpServletResponse response, String url) {
		RequestUtil.sendDispatcher(request, response, url);
	}
	/**
	 * 重定向
	 * @throws IOException 
	 */
	public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
		RequestUtil.sendRedirect(response, url);
	}
	/**
	 * 获取表键名
	 */
	public String getTableKeyName(HttpServletRequest request) throws Exception{
		String tableName = getTableName();

		if(!Tools.notNull(tableName))
			throw new Exception("没有配置表");
		List<String> res = baseService.getColumns(tableName); 
		if(res.size() <= 0)
			throw new Exception("该表 " + tableName + " 没有列 ");
		return (String)res.get(0);
	}
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
		echo(res, page);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/delete.do")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.beforeDo(request, response);

		String key = this.getTableKeyName(request);
		String value = this.getValue(request, key);

		int count = baseService.executeSql("delete from " + getTableName() + " where " + key + "=?", value);
		echo(count);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/add.do")
	public void add(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.beforeDo(request, response);

		Map<?, ?> map = this.getTableParam(request);
		int count = baseService.executeSql("insert into " + getTableName() + "(" + SqlHelp.makeMapKeys(map) + ") values(" + SqlHelp.makeMapPosis(map) + ")  ", map.values().toArray());
		echo(count);
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
		echo(count);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/get.do")
	public void get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.beforeDo(request, response);

		String key = this.getTableKeyName(request);
		String value = this.getValue(request, key);
		
		Map<?, ?> map = baseService.findOne("select * from " + getTableName() + " where " + key + "=?", value);
		echo(map);
	}
	
	
	@RequestMapping("/cols.do")
	public void cols(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.beforeDo(request, response);
		String tableName = getTableName();
		if(!Tools.notNull(tableName)){
			echo(false, "未闻表名");
			return;
		}
		List<String> res = baseService.getColumns(tableName);
		echo(res);
	}
	String getTableName(){
		String tableName = this.getValue(Context.getRequest(), "TABLE_NAME");
		if(! Tools.notNull(tableName)){
			tableName = this.tableName;
		}
		return tableName;
	}
	void beforeDo(HttpServletRequest request, HttpServletResponse response) {
		String tableName = this.getValue(request, "TABLE_NAME");
	}
	

	
}