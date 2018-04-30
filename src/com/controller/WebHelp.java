package com.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import util.Tools;

/**
 * servlet request response printwriter 帮助工具
 * @author Walker
 *
 */
public class WebHelp {

	/**
	 * 获取request所有参数Map
	 * @param request
	 * @return Map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getRequestMap(HttpServletRequest request){
		Map res = new HashMap();
		Enumeration enu=request.getParameterNames();  
		while(enu.hasMoreElements()){  
			String name=(String)enu.nextElement();  
			String value = WebHelp.getKey(request, name);
			res.put(name, value);
		}
		return res;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getParam(HttpServletRequest request, List<Object> colNames){
		Map res = new HashMap();
		for(Object key : colNames){
			res.put(key, WebHelp.getKey(request, key));
		}
		return res;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getKeyParam(HttpServletRequest request, String keyName){
		Map res = new HashMap();
		res.put(keyName, request.getParameter(keyName));
		return res;
	}
	@SuppressWarnings({ })
	public static String getKey(HttpServletRequest request, Object name){
		String value = request.getParameter((String)name);
		if(!Tools.notNull(value)){	//兼容全小写
			value = request.getParameter(((String)name).toLowerCase());
		}
		if(!Tools.notNull(value)){	//兼容全大写
			value = request.getParameter(((String)name).toUpperCase());
		}
		return value;
	}
	
	
}